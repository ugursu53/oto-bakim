import { browser, ExpectedConditions, element, by, ElementFinder } from 'protractor';

export class AracComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-arac div table .btn-danger'));
  title = element.all(by.css('jhi-arac div h2#page-heading span')).first();

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

export class AracUpdatePage {
  pageTitle = element(by.id('jhi-arac-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  plakaNoInput = element(by.id('field_plakaNo'));
  modelYiliInput = element(by.id('field_modelYili'));
  rengiInput = element(by.id('field_rengi'));
  yakitTuruSelect = element(by.id('field_yakitTuru'));
  vitesTuruSelect = element(by.id('field_vitesTuru'));
  motorNoInput = element(by.id('field_motorNo'));
  sasiNoInput = element(by.id('field_sasiNo'));
  kullanimSekliSelect = element(by.id('field_kullanimSekli'));
  aracTipiSelect = element(by.id('field_aracTipi'));
  aciklamaInput = element(by.id('field_aciklama'));
  modelSelect = element(by.id('field_model'));

  async getPageTitle() {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setPlakaNoInput(plakaNo) {
    await this.plakaNoInput.sendKeys(plakaNo);
  }

  async getPlakaNoInput() {
    return await this.plakaNoInput.getAttribute('value');
  }

  async setModelYiliInput(modelYili) {
    await this.modelYiliInput.sendKeys(modelYili);
  }

  async getModelYiliInput() {
    return await this.modelYiliInput.getAttribute('value');
  }

  async setRengiInput(rengi) {
    await this.rengiInput.sendKeys(rengi);
  }

  async getRengiInput() {
    return await this.rengiInput.getAttribute('value');
  }

  async setYakitTuruSelect(yakitTuru) {
    await this.yakitTuruSelect.sendKeys(yakitTuru);
  }

  async getYakitTuruSelect() {
    return await this.yakitTuruSelect.element(by.css('option:checked')).getText();
  }

  async yakitTuruSelectLastOption(timeout?: number) {
    await this.yakitTuruSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async setVitesTuruSelect(vitesTuru) {
    await this.vitesTuruSelect.sendKeys(vitesTuru);
  }

  async getVitesTuruSelect() {
    return await this.vitesTuruSelect.element(by.css('option:checked')).getText();
  }

  async vitesTuruSelectLastOption(timeout?: number) {
    await this.vitesTuruSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async setMotorNoInput(motorNo) {
    await this.motorNoInput.sendKeys(motorNo);
  }

  async getMotorNoInput() {
    return await this.motorNoInput.getAttribute('value');
  }

  async setSasiNoInput(sasiNo) {
    await this.sasiNoInput.sendKeys(sasiNo);
  }

  async getSasiNoInput() {
    return await this.sasiNoInput.getAttribute('value');
  }

  async setKullanimSekliSelect(kullanimSekli) {
    await this.kullanimSekliSelect.sendKeys(kullanimSekli);
  }

  async getKullanimSekliSelect() {
    return await this.kullanimSekliSelect.element(by.css('option:checked')).getText();
  }

  async kullanimSekliSelectLastOption(timeout?: number) {
    await this.kullanimSekliSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async setAracTipiSelect(aracTipi) {
    await this.aracTipiSelect.sendKeys(aracTipi);
  }

  async getAracTipiSelect() {
    return await this.aracTipiSelect.element(by.css('option:checked')).getText();
  }

  async aracTipiSelectLastOption(timeout?: number) {
    await this.aracTipiSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async setAciklamaInput(aciklama) {
    await this.aciklamaInput.sendKeys(aciklama);
  }

  async getAciklamaInput() {
    return await this.aciklamaInput.getAttribute('value');
  }

  async modelSelectLastOption(timeout?: number) {
    await this.modelSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async modelSelectOption(option) {
    await this.modelSelect.sendKeys(option);
  }

  getModelSelect(): ElementFinder {
    return this.modelSelect;
  }

  async getModelSelectedOption() {
    return await this.modelSelect.element(by.css('option:checked')).getText();
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

export class AracDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-arac-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-arac'));

  async getDialogTitle() {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(timeout?: number) {
    await this.confirmButton.click();
  }
}
