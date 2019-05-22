/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { AracCarisiComponentsPage, AracCarisiDeleteDialog, AracCarisiUpdatePage } from './arac-carisi.page-object';

const expect = chai.expect;

describe('AracCarisi e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let aracCarisiUpdatePage: AracCarisiUpdatePage;
  let aracCarisiComponentsPage: AracCarisiComponentsPage;
  let aracCarisiDeleteDialog: AracCarisiDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load AracCarisis', async () => {
    await navBarPage.goToEntity('arac-carisi');
    aracCarisiComponentsPage = new AracCarisiComponentsPage();
    await browser.wait(ec.visibilityOf(aracCarisiComponentsPage.title), 5000);
    expect(await aracCarisiComponentsPage.getTitle()).to.eq('otoBakimApp.aracCarisi.home.title');
  });

  it('should load create AracCarisi page', async () => {
    await aracCarisiComponentsPage.clickOnCreateButton();
    aracCarisiUpdatePage = new AracCarisiUpdatePage();
    expect(await aracCarisiUpdatePage.getPageTitle()).to.eq('otoBakimApp.aracCarisi.home.createOrEditLabel');
    await aracCarisiUpdatePage.cancel();
  });

  it('should create and save AracCarisis', async () => {
    const nbButtonsBeforeCreate = await aracCarisiComponentsPage.countDeleteButtons();

    await aracCarisiComponentsPage.clickOnCreateButton();
    await promise.all([aracCarisiUpdatePage.aracSelectLastOption(), aracCarisiUpdatePage.cariSelectLastOption()]);
    const selectedAktif = aracCarisiUpdatePage.getAktifInput();
    if (await selectedAktif.isSelected()) {
      await aracCarisiUpdatePage.getAktifInput().click();
      expect(await aracCarisiUpdatePage.getAktifInput().isSelected(), 'Expected aktif not to be selected').to.be.false;
    } else {
      await aracCarisiUpdatePage.getAktifInput().click();
      expect(await aracCarisiUpdatePage.getAktifInput().isSelected(), 'Expected aktif to be selected').to.be.true;
    }
    await aracCarisiUpdatePage.save();
    expect(await aracCarisiUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await aracCarisiComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last AracCarisi', async () => {
    const nbButtonsBeforeDelete = await aracCarisiComponentsPage.countDeleteButtons();
    await aracCarisiComponentsPage.clickOnLastDeleteButton();

    aracCarisiDeleteDialog = new AracCarisiDeleteDialog();
    expect(await aracCarisiDeleteDialog.getDialogTitle()).to.eq('otoBakimApp.aracCarisi.delete.question');
    await aracCarisiDeleteDialog.clickOnConfirmButton();

    expect(await aracCarisiComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
