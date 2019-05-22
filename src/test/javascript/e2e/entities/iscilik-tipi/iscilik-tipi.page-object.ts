import { browser, ExpectedConditions, element, by, ElementFinder } from 'protractor';

export class IscilikTipiComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-iscilik-tipi div table .btn-danger'));
  title = element.all(by.css('jhi-iscilik-tipi div h2#page-heading span')).first();

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

export class IscilikTipiUpdatePage {
  pageTitle = element(by.id('jhi-iscilik-tipi-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  adInput = element(by.id('field_ad'));
  varsayilanFiyatInput = element(by.id('field_varsayilanFiyat'));
  grubuSelect = element(by.id('field_grubu'));

  async getPageTitle() {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setAdInput(ad) {
    await this.adInput.sendKeys(ad);
  }

  async getAdInput() {
    return await this.adInput.getAttribute('value');
  }

  async setVarsayilanFiyatInput(varsayilanFiyat) {
    await this.varsayilanFiyatInput.sendKeys(varsayilanFiyat);
  }

  async getVarsayilanFiyatInput() {
    return await this.varsayilanFiyatInput.getAttribute('value');
  }

  async grubuSelectLastOption(timeout?: number) {
    await this.grubuSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async grubuSelectOption(option) {
    await this.grubuSelect.sendKeys(option);
  }

  getGrubuSelect(): ElementFinder {
    return this.grubuSelect;
  }

  async getGrubuSelectedOption() {
    return await this.grubuSelect.element(by.css('option:checked')).getText();
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

export class IscilikTipiDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-iscilikTipi-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-iscilikTipi'));

  async getDialogTitle() {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(timeout?: number) {
    await this.confirmButton.click();
  }
}
