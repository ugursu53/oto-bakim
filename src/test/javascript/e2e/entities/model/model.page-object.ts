import { browser, ExpectedConditions, element, by, ElementFinder } from 'protractor';

export class ModelComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-model div table .btn-danger'));
  title = element.all(by.css('jhi-model div h2#page-heading span')).first();

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

export class ModelUpdatePage {
  pageTitle = element(by.id('jhi-model-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  adInput = element(by.id('field_ad'));
  markaSelect = element(by.id('field_marka'));

  async getPageTitle() {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setAdInput(ad) {
    await this.adInput.sendKeys(ad);
  }

  async getAdInput() {
    return await this.adInput.getAttribute('value');
  }

  async markaSelectLastOption(timeout?: number) {
    await this.markaSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async markaSelectOption(option) {
    await this.markaSelect.sendKeys(option);
  }

  getMarkaSelect(): ElementFinder {
    return this.markaSelect;
  }

  async getMarkaSelectedOption() {
    return await this.markaSelect.element(by.css('option:checked')).getText();
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

export class ModelDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-model-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-model'));

  async getDialogTitle() {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(timeout?: number) {
    await this.confirmButton.click();
  }
}
