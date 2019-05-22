/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { IscilikTipiComponentsPage, IscilikTipiDeleteDialog, IscilikTipiUpdatePage } from './iscilik-tipi.page-object';

const expect = chai.expect;

describe('IscilikTipi e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let iscilikTipiUpdatePage: IscilikTipiUpdatePage;
  let iscilikTipiComponentsPage: IscilikTipiComponentsPage;
  let iscilikTipiDeleteDialog: IscilikTipiDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load IscilikTipis', async () => {
    await navBarPage.goToEntity('iscilik-tipi');
    iscilikTipiComponentsPage = new IscilikTipiComponentsPage();
    await browser.wait(ec.visibilityOf(iscilikTipiComponentsPage.title), 5000);
    expect(await iscilikTipiComponentsPage.getTitle()).to.eq('otoBakimApp.iscilikTipi.home.title');
  });

  it('should load create IscilikTipi page', async () => {
    await iscilikTipiComponentsPage.clickOnCreateButton();
    iscilikTipiUpdatePage = new IscilikTipiUpdatePage();
    expect(await iscilikTipiUpdatePage.getPageTitle()).to.eq('otoBakimApp.iscilikTipi.home.createOrEditLabel');
    await iscilikTipiUpdatePage.cancel();
  });

  it('should create and save IscilikTipis', async () => {
    const nbButtonsBeforeCreate = await iscilikTipiComponentsPage.countDeleteButtons();

    await iscilikTipiComponentsPage.clickOnCreateButton();
    await promise.all([
      iscilikTipiUpdatePage.setAdInput('ad'),
      iscilikTipiUpdatePage.setVarsayilanFiyatInput('5'),
      iscilikTipiUpdatePage.grubuSelectLastOption()
    ]);
    expect(await iscilikTipiUpdatePage.getAdInput()).to.eq('ad', 'Expected Ad value to be equals to ad');
    expect(await iscilikTipiUpdatePage.getVarsayilanFiyatInput()).to.eq('5', 'Expected varsayilanFiyat value to be equals to 5');
    await iscilikTipiUpdatePage.save();
    expect(await iscilikTipiUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await iscilikTipiComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last IscilikTipi', async () => {
    const nbButtonsBeforeDelete = await iscilikTipiComponentsPage.countDeleteButtons();
    await iscilikTipiComponentsPage.clickOnLastDeleteButton();

    iscilikTipiDeleteDialog = new IscilikTipiDeleteDialog();
    expect(await iscilikTipiDeleteDialog.getDialogTitle()).to.eq('otoBakimApp.iscilikTipi.delete.question');
    await iscilikTipiDeleteDialog.clickOnConfirmButton();

    expect(await iscilikTipiComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
