import Foundation
import KeychainAccess

@objc(CDVSecureShare) class CDVSecureShare : CDVPlugin {
    
    private static let KEYCHAIN_GROUP_NAME = "secureshare"

    override func pluginInitialize() {
        super.pluginInitialize()
    }

    @objc(save:)
    func save(_ command: CDVInvokedUrlCommand) {
        if let data = parseArgs(args: command.arguments) {
            let keychain = self.getSharedKeyChainInstance()
            do {
                let serializedData = NSKeyedArchiver.archivedData(withRootObject: data)
                if let appID = getCurrentAppID() {
                    try keychain.set(serializedData, key: appID)
                    self.sendSuccess(callbackId: command.callbackId)
                } else {
                    self.sendError(message: "Unexpected error. Can't get appID!", callbackId: command.callbackId)
                }
            }
            catch let error {
                self.sendError(message: error.localizedDescription, callbackId: command.callbackId)
            }
        } else {
            self.sendError(message: "No data map provided", callbackId: command.callbackId)
        }
    }

    @objc(retrieve:)
    func retrieve(_ command: CDVInvokedUrlCommand){
        if let appID = getCurrentAppID() {
            let result = retrieve(from: appID, callbackId: command.callbackId)
            self.sendSuccess(data: result, callbackId: command.callbackId)
        } else {
            self.sendError(message: "Unexpected error. Can't get appID!", callbackId: command.callbackId)
        }
    }
    
    @objc(retrieveFrom:)
    func retrieveFrom(_ command: CDVInvokedUrlCommand){
        if let appID = command.arguments?[0] as? String {
            let result = retrieve(from: appID, callbackId: command.callbackId)
            self.sendSuccess(data: result, callbackId: command.callbackId)
        } else {
            self.sendSuccess(data: [:], callbackId: command.callbackId)
        }
    }
    
    private func retrieve(from: String, callbackId: String) -> Dictionary<String, String> {
        var result: Dictionary<String, String> = [:];
        let keychain = self.getSharedKeyChainInstance()
        do {
            if let data = try keychain.getData(from) {
                if let deserializedData = NSKeyedUnarchiver.unarchiveObject(with: data) as? Dictionary<String, String> {
                    result = deserializedData
                } else {
                    self.sendError(message: "Error parsing data from keychain", callbackId: callbackId)
                }
            }
        }
        catch let error {
            self.sendError(message: error.localizedDescription, callbackId: callbackId)
        }
        return result;
    }
    
    private func getSharedKeyChainInstance() -> Keychain {
        let appIdentifierPrefix = Bundle.main.infoDictionary?["AppIdentifierPrefix"] as? String ?? ""
        return Keychain(service: "SecureShareService", accessGroup: "\(appIdentifierPrefix)\(CDVSecureShare.KEYCHAIN_GROUP_NAME)")
    }
    
    private func sendSuccess(callbackId: String) {
        let result = CDVPluginResult.init(status: CDVCommandStatus.ok)
        self.commandDelegate.send(result, callbackId: callbackId)
    }
    
    private func sendSuccess(data: [String:Any], callbackId: String) {
        let result = CDVPluginResult.init(status: CDVCommandStatus.ok, messageAs: data)
        self.commandDelegate.send(result, callbackId: callbackId)
    }
    
    private func sendError(message: String, callbackId: String) {
        let error = CDVPluginResult.init(status: CDVCommandStatus.error, messageAs: message)
        self.commandDelegate.send(error, callbackId: callbackId)
    }
    
    private func parseArgs(args: [Any]?) -> Dictionary<String,String>? {
        if (args == nil || args!.count == 0) { return nil }
        if let data = args![0] as? [String: String] {
            return data
        }
        return nil
    }
    
    private func getCurrentAppID() -> String? {
        return Bundle.main.infoDictionary?["CFBundleIdentifier"] as? String
    }

}
