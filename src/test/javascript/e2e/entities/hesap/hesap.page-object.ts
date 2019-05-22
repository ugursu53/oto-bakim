import { browser, ExpectedConditions, element, by, ElementFinder } from 'protractor';

export class HesapComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-hesap div table .btn-danger'));
  title = element.all(by.css('jhi-hesap div h2#page-heading span')).first();

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

export class HesapUpdatePage {
  pageTitle = element(by.id('jhi-hesap-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  bankaInput = element(by.id('field_banka'));
  subeInput = element(by.id('field_sube'));
  hesapNoInput = element(by.id('field_hesapNo'));
  ibanInput = element(by.id('field_iban'));

  async getPageTitle() {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setBankaInput(banka) {
    await this.bankaInput.sendKeys(banka);
  }

  async getBankaInput() {
    return await this.bankaInput.getAttribute('value');
  }

  async setSubeInput(sube) {
    await this.subeInput.sendKeys(sube);
  }

  async getSubeInput() {
    return await this.subeInput.getAttribute('value');
  }

  async setHesapNoInput(hesapNo) {
    await this.hesapNoInput.sendKeys(hesapNo);
  }

  async getHesapNoInput() {
    return await this.hesapNoInput.getAttribute('value');
  }

  async setIbanInput(iban) {
    await this.ibanInput.sendKeys(iban);
  }

  async getIbanInput() {
    return await this.ibanInput.getAttribute('value');
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

export class HesapDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-hesap-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-hesap'));

  async getDialogTitle() {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(timeout?: number) {
    await this.confirmButton.click();
  }
}
