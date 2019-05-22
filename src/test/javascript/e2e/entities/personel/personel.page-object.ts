import { browser, ExpectedConditions, element, by, ElementFinder } from 'protractor';

export class PersonelComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-personel div table .btn-danger'));
  title = element.all(by.css('jhi-personel div h2#page-heading span')).first();

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

export class PersonelUpdatePage {
  pageTitle = element(by.id('jhi-personel-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  adInput = element(by.id('field_ad'));
  soyadInput = element(by.id('field_soyad'));
  goreviInput = element(by.id('field_gorevi'));

  async getPageTitle() {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setAdInput(ad) {
    await this.adInput.sendKeys(ad);
  }

  async getAdInput() {
    return await this.adInput.getAttribute('value');
  }

  async setSoyadInput(soyad) {
    await this.soyadInput.sendKeys(soyad);
  }

  async getSoyadInput() {
    return await this.soyadInput.getAttribute('value');
  }

  async setGoreviInput(gorevi) {
    await this.goreviInput.sendKeys(gorevi);
  }

  async getGoreviInput() {
    return await this.goreviInput.getAttribute('value');
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

export class PersonelDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-personel-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-personel'));

  async getDialogTitle() {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(timeout?: number) {
    await this.confirmButton.click();
  }
}
