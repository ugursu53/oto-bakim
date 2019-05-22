/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ParcaTipiComponentsPage, ParcaTipiDeleteDialog, ParcaTipiUpdatePage } from './parca-tipi.page-object';

const expect = chai.expect;

describe('ParcaTipi e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let parcaTipiUpdatePage: ParcaTipiUpdatePage;
  let parcaTipiComponentsPage: ParcaTipiComponentsPage;
  let parcaTipiDeleteDialog: ParcaTipiDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load ParcaTipis', async () => {
    await navBarPage.goToEntity('parca-tipi');
    parcaTipiComponentsPage = new ParcaTipiComponentsPage();
    await browser.wait(ec.visibilityOf(parcaTipiComponentsPage.title), 5000);
    expect(await parcaTipiComponentsPage.getTitle()).to.eq('otoBakimApp.parcaTipi.home.title');
  });

  it('should load create ParcaTipi page', async () => {
    await parcaTipiComponentsPage.clickOnCreateButton();
    parcaTipiUpdatePage = new ParcaTipiUpdatePage();
    expect(await parcaTipiUpdatePage.getPageTitle()).to.eq('otoBakimApp.parcaTipi.home.createOrEditLabel');
    await parcaTipiUpdatePage.cancel();
  });

  it('should create and save ParcaTipis', async () => {
    const nbButtonsBeforeCreate = await parcaTipiComponentsPage.countDeleteButtons();

    await parcaTipiComponentsPage.clickOnCreateButton();
    await promise.all([
      parcaTipiUpdatePage.setAdInput('ad'),
      parcaTipiUpdatePage.setKoduInput('kodu'),
      parcaTipiUpdatePage.setVarsayilanFiyatiInput('5'),
      parcaTipiUpdatePage.setAciklamaInput('aciklama')
    ]);
    expect(await parcaTipiUpdatePage.getAdInput()).to.eq('ad', 'Expected Ad value to be equals to ad');
    expect(await parcaTipiUpdatePage.getKoduInput()).to.eq('kodu', 'Expected Kodu value to be equals to kodu');
    expect(await parcaTipiUpdatePage.getVarsayilanFiyatiInput()).to.eq('5', 'Expected varsayilanFiyati value to be equals to 5');
    expect(await parcaTipiUpdatePage.getAciklamaInput()).to.eq('aciklama', 'Expected Aciklama value to be equals to aciklama');
    await parcaTipiUpdatePage.save();
    expect(await parcaTipiUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await parcaTipiComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last ParcaTipi', async () => {
    const nbButtonsBeforeDelete = await parcaTipiComponentsPage.countDeleteButtons();
    await parcaTipiComponentsPage.clickOnLastDeleteButton();

    parcaTipiDeleteDialog = new ParcaTipiDeleteDialog();
    expect(await parcaTipiDeleteDialog.getDialogTitle()).to.eq('otoBakimApp.parcaTipi.delete.question');
    await parcaTipiDeleteDialog.clickOnConfirmButton();

    expect(await parcaTipiComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
