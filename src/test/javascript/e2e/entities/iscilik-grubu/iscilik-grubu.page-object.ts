import { browser, ExpectedConditions, element, by, ElementFinder } from 'protractor';

export class IscilikGrubuComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-iscilik-grubu div table .btn-danger'));
  title = element.all(by.css('jhi-iscilik-grubu div h2#page-heading span')).first();

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

export class IscilikGrubuUpdatePage {
  pageTitle = element(by.id('jhi-iscilik-grubu-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  adInput = element(by.id('field_ad'));

  async getPageTitle() {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setAdInput(ad) {
    await this.adInput.sendKeys(ad);
  }

  async getAdInput() {
    return await this.adInput.getAttribute('value');
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

export class IscilikGrubuDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-iscilikGrubu-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-iscilikGrubu'));

  async getDialogTitle() {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(timeout?: number) {
    await this.confirmButton.click();
  }
}
