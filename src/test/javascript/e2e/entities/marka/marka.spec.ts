/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { MarkaComponentsPage, MarkaDeleteDialog, MarkaUpdatePage } from './marka.page-object';

const expect = chai.expect;

describe('Marka e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let markaUpdatePage: MarkaUpdatePage;
  let markaComponentsPage: MarkaComponentsPage;
  let markaDeleteDialog: MarkaDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Markas', async () => {
    await navBarPage.goToEntity('marka');
    markaComponentsPage = new MarkaComponentsPage();
    await browser.wait(ec.visibilityOf(markaComponentsPage.title), 5000);
    expect(await markaComponentsPage.getTitle()).to.eq('otoBakimApp.marka.home.title');
  });

  it('should load create Marka page', async () => {
    await markaComponentsPage.clickOnCreateButton();
    markaUpdatePage = new MarkaUpdatePage();
    expect(await markaUpdatePage.getPageTitle()).to.eq('otoBakimApp.marka.home.createOrEditLabel');
    await markaUpdatePage.cancel();
  });

  it('should create and save Markas', async () => {
    const nbButtonsBeforeCreate = await markaComponentsPage.countDeleteButtons();

    await markaComponentsPage.clickOnCreateButton();
    await promise.all([markaUpdatePage.setAdInput('ad')]);
    expect(await markaUpdatePage.getAdInput()).to.eq('ad', 'Expected Ad value to be equals to ad');
    await markaUpdatePage.save();
    expect(await markaUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await markaComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Marka', async () => {
    const nbButtonsBeforeDelete = await markaComponentsPage.countDeleteButtons();
    await markaComponentsPage.clickOnLastDeleteButton();

    markaDeleteDialog = new MarkaDeleteDialog();
    expect(await markaDeleteDialog.getDialogTitle()).to.eq('otoBakimApp.marka.delete.question');
    await markaDeleteDialog.clickOnConfirmButton();

    expect(await markaComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
