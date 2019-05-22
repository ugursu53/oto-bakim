/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { IscilikComponentsPage, IscilikDeleteDialog, IscilikUpdatePage } from './iscilik.page-object';

const expect = chai.expect;

describe('Iscilik e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let iscilikUpdatePage: IscilikUpdatePage;
  let iscilikComponentsPage: IscilikComponentsPage;
  let iscilikDeleteDialog: IscilikDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Isciliks', async () => {
    await navBarPage.goToEntity('iscilik');
    iscilikComponentsPage = new IscilikComponentsPage();
    await browser.wait(ec.visibilityOf(iscilikComponentsPage.title), 5000);
    expect(await iscilikComponentsPage.getTitle()).to.eq('otoBakimApp.iscilik.home.title');
  });

  it('should load create Iscilik page', async () => {
    await iscilikComponentsPage.clickOnCreateButton();
    iscilikUpdatePage = new IscilikUpdatePage();
    expect(await iscilikUpdatePage.getPageTitle()).to.eq('otoBakimApp.iscilik.home.createOrEditLabel');
    await iscilikUpdatePage.cancel();
  });

  it('should create and save Isciliks', async () => {
    const nbButtonsBeforeCreate = await iscilikComponentsPage.countDeleteButtons();

    await iscilikComponentsPage.clickOnCreateButton();
    await promise.all([
      iscilikUpdatePage.setAciklamaInput('aciklama'),
      iscilikUpdatePage.setFiyatInput('5'),
      iscilikUpdatePage.setIskontoInput('5'),
      iscilikUpdatePage.isEmriSelectLastOption(),
      iscilikUpdatePage.tipiSelectLastOption(),
      iscilikUpdatePage.personelSelectLastOption()
    ]);
    expect(await iscilikUpdatePage.getAciklamaInput()).to.eq('aciklama', 'Expected Aciklama value to be equals to aciklama');
    expect(await iscilikUpdatePage.getFiyatInput()).to.eq('5', 'Expected fiyat value to be equals to 5');
    expect(await iscilikUpdatePage.getIskontoInput()).to.eq('5', 'Expected iskonto value to be equals to 5');
    await iscilikUpdatePage.save();
    expect(await iscilikUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await iscilikComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Iscilik', async () => {
    const nbButtonsBeforeDelete = await iscilikComponentsPage.countDeleteButtons();
    await iscilikComponentsPage.clickOnLastDeleteButton();

    iscilikDeleteDialog = new IscilikDeleteDialog();
    expect(await iscilikDeleteDialog.getDialogTitle()).to.eq('otoBakimApp.iscilik.delete.question');
    await iscilikDeleteDialog.clickOnConfirmButton();

    expect(await iscilikComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
