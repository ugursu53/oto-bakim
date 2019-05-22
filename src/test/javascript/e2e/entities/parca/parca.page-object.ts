import { browser, ExpectedConditions, element, by, ElementFinder } from 'protractor';

export class ParcaComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-parca div table .btn-danger'));
  title = element.all(by.css('jhi-parca div h2#page-heading span')).first();

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

export class ParcaUpdatePage {
  pageTitle = element(by.id('jhi-parca-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  fiyatiInput = element(by.id('field_fiyati'));
  isEmriSelect = element(by.id('field_isEmri'));
  tipiSelect = element(by.id('field_tipi'));

  async getPageTitle() {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setFiyatiInput(fiyati) {
    await this.fiyatiInput.sendKeys(fiyati);
  }

  async getFiyatiInput() {
    return await this.fiyatiInput.getAttribute('value');
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

export class ParcaDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-parca-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-parca'));

  async getDialogTitle() {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(timeout?: number) {
    await this.confirmButton.click();
  }
}
