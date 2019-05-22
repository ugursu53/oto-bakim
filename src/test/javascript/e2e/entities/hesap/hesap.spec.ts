/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { HesapComponentsPage, HesapDeleteDialog, HesapUpdatePage } from './hesap.page-object';

const expect = chai.expect;

describe('Hesap e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let hesapUpdatePage: HesapUpdatePage;
  let hesapComponentsPage: HesapComponentsPage;
  let hesapDeleteDialog: HesapDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Hesaps', async () => {
    await navBarPage.goToEntity('hesap');
    hesapComponentsPage = new HesapComponentsPage();
    await browser.wait(ec.visibilityOf(hesapComponentsPage.title), 5000);
    expect(await hesapComponentsPage.getTitle()).to.eq('otoBakimApp.hesap.home.title');
  });

  it('should load create Hesap page', async () => {
    await hesapComponentsPage.clickOnCreateButton();
    hesapUpdatePage = new HesapUpdatePage();
    expect(await hesapUpdatePage.getPageTitle()).to.eq('otoBakimApp.hesap.home.createOrEditLabel');
    await hesapUpdatePage.cancel();
  });

  it('should create and save Hesaps', async () => {
    const nbButtonsBeforeCreate = await hesapComponentsPage.countDeleteButtons();

    await hesapComponentsPage.clickOnCreateButton();
    await promise.all([
      hesapUpdatePage.setBankaInput('banka'),
      hesapUpdatePage.setSubeInput('sube'),
      hesapUpdatePage.setHesapNoInput('hesapNo'),
      hesapUpdatePage.setIbanInput('iban')
    ]);
    expect(await hesapUpdatePage.getBankaInput()).to.eq('banka', 'Expected Banka value to be equals to banka');
    expect(await hesapUpdatePage.getSubeInput()).to.eq('sube', 'Expected Sube value to be equals to sube');
    expect(await hesapUpdatePage.getHesapNoInput()).to.eq('hesapNo', 'Expected HesapNo value to be equals to hesapNo');
    expect(await hesapUpdatePage.getIbanInput()).to.eq('iban', 'Expected Iban value to be equals to iban');
    await hesapUpdatePage.save();
    expect(await hesapUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await hesapComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Hesap', async () => {
    const nbButtonsBeforeDelete = await hesapComponentsPage.countDeleteButtons();
    await hesapComponentsPage.clickOnLastDeleteButton();

    hesapDeleteDialog = new HesapDeleteDialog();
    expect(await hesapDeleteDialog.getDialogTitle()).to.eq('otoBakimApp.hesap.delete.question');
    await hesapDeleteDialog.clickOnConfirmButton();

    expect(await hesapComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
