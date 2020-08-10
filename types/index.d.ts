export interface SecureSharePlugin {
  /**
   * @description      Save data to be shared to other apps.
   * 
   * @param            data string to string map holding the data to be saved
   * 
   * @returns          A promise of void
   * 
   * @errors           SECSHARE__UNKNOWN_ERROR: Unknown error prevented execution
   */
  save(data: Record<string, string>): Promise<void>;

  /**
   * @description      Get data saved in secure share storage
   * 
   * @returns          A promise of a string to string map with the data saved
   * 
   * @errors           SECSHARE__UNKNOWN_ERROR: Unknown error prevented execution
   */
  retrieve(): Promise<Record<string, string>>;


  /**
   * @description      Get data saved in secure share storage
   * 
   * @param            pacakgeName package to retrieve shared data from
   * 
   * @returns          A promise of a string to string map with the data saved
   * 
   * @errors           SECSHARE__UNKNOWN_ERROR: Unknown error prevented execution
   */
  retrieveFrom(packageName: string): Promise<Record<string, string>>;

}
