/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ModelComponentsPage, ModelDeleteDialog, ModelUpdatePage } from './model.page-object';

const expect = chai.expect;

describe('Model e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let modelUpdatePage: ModelUpdatePage;
  let modelComponentsPage: ModelComponentsPage;
  let modelDeleteDialog: ModelDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Models', async () => {
    await navBarPage.goToEntity('model');
    modelComponentsPage = new ModelComponentsPage();
    await browser.wait(ec.visibilityOf(modelComponentsPage.title), 5000);
    expect(await modelComponentsPage.getTitle()).to.eq('otoBakimApp.model.home.title');
  });

  it('should load create Model page', async () => {
    await modelComponentsPage.clickOnCreateButton();
    modelUpdatePage = new ModelUpdatePage();
    expect(await modelUpdatePage.getPageTitle()).to.eq('otoBakimApp.model.home.createOrEditLabel');
    await modelUpdatePage.cancel();
  });

  it('should create and save Models', async () => {
    const nbButtonsBeforeCreate = await modelComponentsPage.countDeleteButtons();

    await modelComponentsPage.clickOnCreateButton();
    await promise.all([modelUpdatePage.setAdInput('ad'), modelUpdatePage.markaSelectLastOption()]);
    expect(await modelUpdatePage.getAdInput()).to.eq('ad', 'Expected Ad value to be equals to ad');
    await modelUpdatePage.save();
    expect(await modelUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await modelComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Model', async () => {
    const nbButtonsBeforeDelete = await modelComponentsPage.countDeleteButtons();
    await modelComponentsPage.clickOnLastDeleteButton();

    modelDeleteDialog = new ModelDeleteDialog();
    expect(await modelDeleteDialog.getDialogTitle()).to.eq('otoBakimApp.model.delete.question');
    await modelDeleteDialog.clickOnConfirmButton();

    expect(await modelComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
