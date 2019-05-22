import { browser, ExpectedConditions, element, by, ElementFinder } from 'protractor';

export class CariComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-cari div table .btn-danger'));
  title = element.all(by.css('jhi-cari div h2#page-heading span')).first();

  async clickOnCreateButton(timeout?: number) {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(timeout?: number) {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons() {
    return this.deleteButtons.count();
  }

  async getTitle() {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class CariUpdatePage {
  pageTitle = element(by.id('jhi-cari-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  tipiSelect = element(by.id('field_tipi'));
  kisiTipiSelect = element(by.id('field_kisiTipi'));
  aktifInput = element(by.id('field_aktif'));
  adInput = element(by.id('field_ad'));
  adresInput = element(by.id('field_adres'));
  telefonInput = element(by.id('field_telefon'));
  tcNoInput = element(by.id('field_tcNo'));
  vergiNoInput = element(by.id('field_vergiNo'));
  yetkiliInput = element(by.id('field_yetkili'));
  faxInput = element(by.id('field_fax'));
  epostaInput = element(by.id('field_eposta'));
  webAdresiInput = element(by.id('field_webAdresi'));
  iskontoInput = element(by.id('field_iskonto'));
  efaturaKullanimiInput = element(by.id('field_efaturaKullanimi'));
  aciklamaInput = element(by.id('field_aciklama'));
  varsayilanIsEmriTipiSelect = element(by.id('field_varsayilanIsEmriTipi'));
  hesapSelect = element(by.id('field_hesap'));

  async getPageTitle() {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setTipiSelect(tipi) {
    await this.tipiSelect.sendKeys(tipi);
  }

  async getTipiSelect() {
    return await this.tipiSelect.element(by.css('option:checked')).getText();
  }

  async tipiSelectLastOption(timeout?: number) {
    await this.tipiSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async setKisiTipiSelect(kisiTipi) {
    await this.kisiTipiSelect.sendKeys(kisiTipi);
  }

  async getKisiTipiSelect() {
    return await this.kisiTipiSelect.element(by.css('option:checked')).getText();
  }

  async kisiTipiSelectLastOption(timeout?: number) {
    await this.kisiTipiSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  getAktifInput(timeout?: number) {
    return this.aktifInput;
  }
  async setAdInput(ad) {
    await this.adInput.sendKeys(ad);
  }

  async getAdInput() {
    return await this.adInput.getAttribute('value');
  }

  async setAdresInput(adres) {
    await this.adresInput.sendKeys(adres);
  }

  async getAdresInput() {
    return await this.adresInput.getAttribute('value');
  }

  async setTelefonInput(telefon) {
    await this.telefonInput.sendKeys(telefon);
  }

  async getTelefonInput() {
    return await this.telefonInput.getAttribute('value');
  }

  async setTcNoInput(tcNo) {
    await this.tcNoInput.sendKeys(tcNo);
  }

  async getTcNoInput() {
    return await this.tcNoInput.getAttribute('value');
  }

  async setVergiNoInput(vergiNo) {
    await this.vergiNoInput.sendKeys(vergiNo);
  }

  async getVergiNoInput() {
    return await this.vergiNoInput.getAttribute('value');
  }

  async setYetkiliInput(yetkili) {
    await this.yetkiliInput.sendKeys(yetkili);
  }

  async getYetkiliInput() {
    return await this.yetkiliInput.getAttribute('value');
  }

  async setFaxInput(fax) {
    await this.faxInput.sendKeys(fax);
  }

  async getFaxInput() {
    return await this.faxInput.getAttribute('value');
  }

  async setEpostaInput(eposta) {
    await this.epostaInput.sendKeys(eposta);
  }

  async getEpostaInput() {
    return await this.epostaInput.getAttribute('value');
  }

  async setWebAdresiInput(webAdresi) {
    await this.webAdresiInput.sendKeys(webAdresi);
  }

  async getWebAdresiInput() {
    return await this.webAdresiInput.getAttribute('value');
  }

  async setIskontoInput(iskonto) {
    await this.iskontoInput.sendKeys(iskonto);
  }

  async getIskontoInput() {
    return await this.iskontoInput.getAttribute('value');
  }

  getEfaturaKullanimiInput(timeout?: number) {
    return this.efaturaKullanimiInput;
  }
  async setAciklamaInput(aciklama) {
    await this.aciklamaInput.sendKeys(aciklama);
  }

  async getAciklamaInput() {
    return await this.aciklamaInput.getAttribute('value');
  }

  async setVarsayilanIsEmriTipiSelect(varsayilanIsEmriTipi) {
    await this.varsayilanIsEmriTipiSelect.sendKeys(varsayilanIsEmriTipi);
  }

  async getVarsayilanIsEmriTipiSelect() {
    return await this.varsayilanIsEmriTipiSelect.element(by.css('option:checked')).getText();
  }

  async varsayilanIsEmriTipiSelectLastOption(timeout?: number) {
    await this.varsayilanIsEmriTipiSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async hesapSelectLastOption(timeout?: number) {
    await this.hesapSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async hesapSelectOption(option) {
    await this.hesapSelect.sendKeys(option);
  }

  getHesapSelect(): ElementFinder {
    return this.hesapSelect;
  }

  async getHesapSelectedOption() {
    return await this.hesapSelect.element(by.css('option:checked')).getText();
  }

  async save(timeout?: number) {
    await this.saveButton.click();
  }

  async cancel(timeout?: number) {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class CariDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-cari-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-cari'));

  async getDialogTitle() {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(timeout?: number) {
    await this.confirmButton.click();
  }
}
