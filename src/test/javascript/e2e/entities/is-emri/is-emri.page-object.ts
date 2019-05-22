import { browser, ExpectedConditions, element, by, ElementFinder } from 'protractor';

export class IsEmriComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-is-emri div table .btn-danger'));
  title = element.all(by.css('jhi-is-emri div h2#page-heading span')).first();

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

export class IsEmriUpdatePage {
  pageTitle = element(by.id('jhi-is-emri-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  gelisZamaniInput = element(by.id('field_gelisZamani'));
  aciklamaInput = element(by.id('field_aciklama'));
  kabulTarihiInput = element(by.id('field_kabulTarihi'));
  tipiSelect = element(by.id('field_tipi'));
  aracSelect = element(by.id('field_arac'));

  async getPageTitle() {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setGelisZamaniInput(gelisZamani) {
    await this.gelisZamaniInput.sendKeys(gelisZamani);
  }

  async getGelisZamaniInput() {
    return await this.gelisZamaniInput.getAttribute('value');
  }

  async setAciklamaInput(aciklama) {
    await this.aciklamaInput.sendKeys(aciklama);
  }

  async getAciklamaInput() {
    return await this.aciklamaInput.getAttribute('value');
  }

  async setKabulTarihiInput(kabulTarihi) {
    await this.kabulTarihiInput.sendKeys(kabulTarihi);
  }

  async getKabulTarihiInput() {
    return await this.kabulTarihiInput.getAttribute('value');
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

export class IsEmriDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-isEmri-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-isEmri'));

  async getDialogTitle() {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(timeout?: number) {
    await this.confirmButton.click();
  }
}
