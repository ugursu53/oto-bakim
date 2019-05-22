import { browser, ExpectedConditions, element, by, ElementFinder } from 'protractor';

export class ParcaTipiComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-parca-tipi div table .btn-danger'));
  title = element.all(by.css('jhi-parca-tipi div h2#page-heading span')).first();

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

export class ParcaTipiUpdatePage {
  pageTitle = element(by.id('jhi-parca-tipi-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  adInput = element(by.id('field_ad'));
  koduInput = element(by.id('field_kodu'));
  varsayilanFiyatiInput = element(by.id('field_varsayilanFiyati'));
  aciklamaInput = element(by.id('field_aciklama'));

  async getPageTitle() {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setAdInput(ad) {
    await this.adInput.sendKeys(ad);
  }

  async getAdInput() {
    return await this.adInput.getAttribute('value');
  }

  async setKoduInput(kodu) {
    await this.koduInput.sendKeys(kodu);
  }

  async getKoduInput() {
    return await this.koduInput.getAttribute('value');
  }

  async setVarsayilanFiyatiInput(varsayilanFiyati) {
    await this.varsayilanFiyatiInput.sendKeys(varsayilanFiyati);
  }

  async getVarsayilanFiyatiInput() {
    return await this.varsayilanFiyatiInput.getAttribute('value');
  }

  async setAciklamaInput(aciklama) {
    await this.aciklamaInput.sendKeys(aciklama);
  }

  async getAciklamaInput() {
    return await this.aciklamaInput.getAttribute('value');
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

export class ParcaTipiDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-parcaTipi-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-parcaTipi'));

  async getDialogTitle() {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(timeout?: number) {
    await this.confirmButton.click();
  }
}
