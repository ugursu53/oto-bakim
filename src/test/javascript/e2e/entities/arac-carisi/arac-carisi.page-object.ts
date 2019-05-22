import { browser, ExpectedConditions, element, by, ElementFinder } from 'protractor';

export class AracCarisiComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-arac-carisi div table .btn-danger'));
  title = element.all(by.css('jhi-arac-carisi div h2#page-heading span')).first();

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

export class AracCarisiUpdatePage {
  pageTitle = element(by.id('jhi-arac-carisi-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  aktifInput = element(by.id('field_aktif'));
  aracSelect = element(by.id('field_arac'));
  cariSelect = element(by.id('field_cari'));

  async getPageTitle() {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  getAktifInput(timeout?: number) {
    return this.aktifInput;
  }

  async aracSelectLastOption(timeout?: number) {
    await this.aracSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async aracSelectOption(option) {
    await this.aracSelect.sendKeys(option);
  }

  getAracSelect(): ElementFinder {
    return this.aracSelect;
  }

  async getAracSelectedOption() {
    return await this.aracSelect.element(by.css('option:checked')).getText();
  }

  async cariSelectLastOption(timeout?: number) {
    await this.cariSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async cariSelectOption(option) {
    await this.cariSelect.sendKeys(option);
  }

  getCariSelect(): ElementFinder {
    return this.cariSelect;
  }

  async getCariSelectedOption() {
    return await this.cariSelect.element(by.css('option:checked')).getText();
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

export class AracCarisiDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-aracCarisi-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-aracCarisi'));

  async getDialogTitle() {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(timeout?: number) {
    await this.confirmButton.click();
  }
}
