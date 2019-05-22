/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { IsEmriComponentsPage, IsEmriDeleteDialog, IsEmriUpdatePage } from './is-emri.page-object';

const expect = chai.expect;

describe('IsEmri e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let isEmriUpdatePage: IsEmriUpdatePage;
  let isEmriComponentsPage: IsEmriComponentsPage;
  let isEmriDeleteDialog: IsEmriDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load IsEmris', async () => {
    await navBarPage.goToEntity('is-emri');
    isEmriComponentsPage = new IsEmriComponentsPage();
    await browser.wait(ec.visibilityOf(isEmriComponentsPage.title), 5000);
    expect(await isEmriComponentsPage.getTitle()).to.eq('otoBakimApp.isEmri.home.title');
  });

  it('should load create IsEmri page', async () => {
    await isEmriComponentsPage.clickOnCreateButton();
    isEmriUpdatePage = new IsEmriUpdatePage();
    expect(await isEmriUpdatePage.getPageTitle()).to.eq('otoBakimApp.isEmri.home.createOrEditLabel');
    await isEmriUpdatePage.cancel();
  });

  it('should create and save IsEmris', async () => {
    const nbButtonsBeforeCreate = await isEmriComponentsPage.countDeleteButtons();

    await isEmriComponentsPage.clickOnCreateButton();
    await promise.all([
      isEmriUpdatePage.setGelisZamaniInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      isEmriUpdatePage.setAciklamaInput('aciklama'),
      isEmriUpdatePage.setKabulTarihiInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      isEmriUpdatePage.tipiSelectLastOption(),
      isEmriUpdatePage.aracSelectLastOption()
    ]);
    expect(await isEmriUpdatePage.getGelisZamaniInput()).to.contain(
      '2001-01-01T02:30',
      'Expected gelisZamani value to be equals to 2000-12-31'
    );
    expect(await isEmriUpdatePage.getAciklamaInput()).to.eq('aciklama', 'Expected Aciklama value to be equals to aciklama');
    expect(await isEmriUpdatePage.getKabulTarihiInput()).to.contain(
      '2001-01-01T02:30',
      'Expected kabulTarihi value to be equals to 2000-12-31'
    );
    await isEmriUpdatePage.save();
    expect(await isEmriUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await isEmriComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last IsEmri', async () => {
    const nbButtonsBeforeDelete = await isEmriComponentsPage.countDeleteButtons();
    await isEmriComponentsPage.clickOnLastDeleteButton();

    isEmriDeleteDialog = new IsEmriDeleteDialog();
    expect(await isEmriDeleteDialog.getDialogTitle()).to.eq('otoBakimApp.isEmri.delete.question');
    await isEmriDeleteDialog.clickOnConfirmButton();

    expect(await isEmriComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
