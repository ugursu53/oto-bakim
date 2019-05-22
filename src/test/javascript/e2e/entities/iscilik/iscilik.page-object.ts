import { browser, ExpectedConditions, element, by, ElementFinder } from 'protractor';

export class IscilikComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-iscilik div table .btn-danger'));
  title = element.all(by.css('jhi-iscilik div h2#page-heading span')).first();

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

export class IscilikUpdatePage {
  pageTitle = element(by.id('jhi-iscilik-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  aciklamaInput = element(by.id('field_aciklama'));
  fiyatInput = element(by.id('field_fiyat'));
  iskontoInput = element(by.id('field_iskonto'));
  isEmriSelect = element(by.id('field_isEmri'));
  tipiSelect = element(by.id('field_tipi'));
  personelSelect = element(by.id('field_personel'));

  async getPageTitle() {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setAciklamaInput(aciklama) {
    await this.aciklamaInput.sendKeys(aciklama);
  }

  async getAciklamaInput() {
    return await this.aciklamaInput.getAttribute('value');
  }

  async setFiyatInput(fiyat) {
    await this.fiyatInput.sendKeys(fiyat);
  }

  async getFiyatInput() {
    return await this.fiyatInput.getAttribute('value');
  }

  async setIskontoInput(iskonto) {
    await this.iskontoInput.sendKeys(iskonto);
  }

  async getIskontoInput() {
    return await this.iskontoInput.getAttribute('value');
  }

  async isEmriSelectLastOption(timeout?: number) {
    await this.isEmriSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async isEmriSelectOption(option) {
    await this.isEmriSelect.sendKeys(option);
  }

  getIsEmriSelect(): ElementFinder {
    return this.isEmriSelect;
  }

  async getIsEmriSelectedOption() {
    return await this.isEmriSelect.element(by.css('option:checked')).getText();
  }

  async tipiSelectLastOption(timeout?: number) {
    await this.tipiSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async tipiSelectOption(option) {
    await this.tipiSelect.sendKeys(option);
  }

  getTipiSelect(): ElementFinder {
    return this.tipiSelect;
  }

  async getTipiSelectedOption() {
    return await this.tipiSelect.element(by.css('option:checked')).getText();
  }

  async personelSelectLastOption(timeout?: number) {
    await this.personelSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async personelSelectOption(option) {
    await this.personelSelect.sendKeys(option);
  }

  getPersonelSelect(): ElementFinder {
    return this.personelSelect;
  }

  async getPersonelSelectedOption() {
    return await this.personelSelect.element(by.css('option:checked')).getText();
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

export class IscilikDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-iscilik-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-iscilik'));

  async getDialogTitle() {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(timeout?: number) {
    await this.confirmButton.click();
  }
}
