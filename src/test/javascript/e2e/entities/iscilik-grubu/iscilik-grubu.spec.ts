/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { IscilikGrubuComponentsPage, IscilikGrubuDeleteDialog, IscilikGrubuUpdatePage } from './iscilik-grubu.page-object';

const expect = chai.expect;

describe('IscilikGrubu e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let iscilikGrubuUpdatePage: IscilikGrubuUpdatePage;
  let iscilikGrubuComponentsPage: IscilikGrubuComponentsPage;
  let iscilikGrubuDeleteDialog: IscilikGrubuDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load IscilikGrubus', async () => {
    await navBarPage.goToEntity('iscilik-grubu');
    iscilikGrubuComponentsPage = new IscilikGrubuComponentsPage();
    await browser.wait(ec.visibilityOf(iscilikGrubuComponentsPage.title), 5000);
    expect(await iscilikGrubuComponentsPage.getTitle()).to.eq('otoBakimApp.iscilikGrubu.home.title');
  });

  it('should load create IscilikGrubu page', async () => {
    await iscilikGrubuComponentsPage.clickOnCreateButton();
    iscilikGrubuUpdatePage = new IscilikGrubuUpdatePage();
    expect(await iscilikGrubuUpdatePage.getPageTitle()).to.eq('otoBakimApp.iscilikGrubu.home.createOrEditLabel');
    await iscilikGrubuUpdatePage.cancel();
  });

  it('should create and save IscilikGrubus', async () => {
    const nbButtonsBeforeCreate = await iscilikGrubuComponentsPage.countDeleteButtons();

    await iscilikGrubuComponentsPage.clickOnCreateButton();
    await promise.all([iscilikGrubuUpdatePage.setAdInput('ad')]);
    expect(await iscilikGrubuUpdatePage.getAdInput()).to.eq('ad', 'Expected Ad value to be equals to ad');
    await iscilikGrubuUpdatePage.save();
    expect(await iscilikGrubuUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await iscilikGrubuComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last IscilikGrubu', async () => {
    const nbButtonsBeforeDelete = await iscilikGrubuComponentsPage.countDeleteButtons();
    await iscilikGrubuComponentsPage.clickOnLastDeleteButton();

    iscilikGrubuDeleteDialog = new IscilikGrubuDeleteDialog();
    expect(await iscilikGrubuDeleteDialog.getDialogTitle()).to.eq('otoBakimApp.iscilikGrubu.delete.question');
    await iscilikGrubuDeleteDialog.clickOnConfirmButton();

    expect(await iscilikGrubuComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
