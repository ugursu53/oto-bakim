/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ParcaComponentsPage, ParcaDeleteDialog, ParcaUpdatePage } from './parca.page-object';

const expect = chai.expect;

describe('Parca e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let parcaUpdatePage: ParcaUpdatePage;
  let parcaComponentsPage: ParcaComponentsPage;
  let parcaDeleteDialog: ParcaDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Parcas', async () => {
    await navBarPage.goToEntity('parca');
    parcaComponentsPage = new ParcaComponentsPage();
    await browser.wait(ec.visibilityOf(parcaComponentsPage.title), 5000);
    expect(await parcaComponentsPage.getTitle()).to.eq('otoBakimApp.parca.home.title');
  });

  it('should load create Parca page', async () => {
    await parcaComponentsPage.clickOnCreateButton();
    parcaUpdatePage = new ParcaUpdatePage();
    expect(await parcaUpdatePage.getPageTitle()).to.eq('otoBakimApp.parca.home.createOrEditLabel');
    await parcaUpdatePage.cancel();
  });

  it('should create and save Parcas', async () => {
    const nbButtonsBeforeCreate = await parcaComponentsPage.countDeleteButtons();

    await parcaComponentsPage.clickOnCreateButton();
    await promise.all([
      parcaUpdatePage.setFiyatiInput('5'),
      parcaUpdatePage.isEmriSelectLastOption(),
      parcaUpdatePage.tipiSelectLastOption()
    ]);
    expect(await parcaUpdatePage.getFiyatiInput()).to.eq('5', 'Expected fiyati value to be equals to 5');
    await parcaUpdatePage.save();
    expect(await parcaUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await parcaComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Parca', async () => {
    const nbButtonsBeforeDelete = await parcaComponentsPage.countDeleteButtons();
    await parcaComponentsPage.clickOnLastDeleteButton();

    parcaDeleteDialog = new ParcaDeleteDialog();
    expect(await parcaDeleteDialog.getDialogTitle()).to.eq('otoBakimApp.parca.delete.question');
    await parcaDeleteDialog.clickOnConfirmButton();

    expect(await parcaComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
