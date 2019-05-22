/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { AracComponentsPage, AracDeleteDialog, AracUpdatePage } from './arac.page-object';

const expect = chai.expect;

describe('Arac e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let aracUpdatePage: AracUpdatePage;
  let aracComponentsPage: AracComponentsPage;
  let aracDeleteDialog: AracDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Aracs', async () => {
    await navBarPage.goToEntity('arac');
    aracComponentsPage = new AracComponentsPage();
    await browser.wait(ec.visibilityOf(aracComponentsPage.title), 5000);
    expect(await aracComponentsPage.getTitle()).to.eq('otoBakimApp.arac.home.title');
  });

  it('should load create Arac page', async () => {
    await aracComponentsPage.clickOnCreateButton();
    aracUpdatePage = new AracUpdatePage();
    expect(await aracUpdatePage.getPageTitle()).to.eq('otoBakimApp.arac.home.createOrEditLabel');
    await aracUpdatePage.cancel();
  });

  it('should create and save Aracs', async () => {
    const nbButtonsBeforeCreate = await aracComponentsPage.countDeleteButtons();

    await aracComponentsPage.clickOnCreateButton();
    await promise.all([
      aracUpdatePage.setPlakaNoInput('plakaNo'),
      aracUpdatePage.setModelYiliInput('5'),
      aracUpdatePage.setRengiInput('rengi'),
      aracUpdatePage.yakitTuruSelectLastOption(),
      aracUpdatePage.vitesTuruSelectLastOption(),
      aracUpdatePage.setMotorNoInput('motorNo'),
      aracUpdatePage.setSasiNoInput('sasiNo'),
      aracUpdatePage.kullanimSekliSelectLastOption(),
      aracUpdatePage.aracTipiSelectLastOption(),
      aracUpdatePage.setAciklamaInput('aciklama'),
      aracUpdatePage.modelSelectLastOption()
    ]);
    expect(await aracUpdatePage.getPlakaNoInput()).to.eq('plakaNo', 'Expected PlakaNo value to be equals to plakaNo');
    expect(await aracUpdatePage.getModelYiliInput()).to.eq('5', 'Expected modelYili value to be equals to 5');
    expect(await aracUpdatePage.getRengiInput()).to.eq('rengi', 'Expected Rengi value to be equals to rengi');
    expect(await aracUpdatePage.getMotorNoInput()).to.eq('motorNo', 'Expected MotorNo value to be equals to motorNo');
    expect(await aracUpdatePage.getSasiNoInput()).to.eq('sasiNo', 'Expected SasiNo value to be equals to sasiNo');
    expect(await aracUpdatePage.getAciklamaInput()).to.eq('aciklama', 'Expected Aciklama value to be equals to aciklama');
    await aracUpdatePage.save();
    expect(await aracUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await aracComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Arac', async () => {
    const nbButtonsBeforeDelete = await aracComponentsPage.countDeleteButtons();
    await aracComponentsPage.clickOnLastDeleteButton();

    aracDeleteDialog = new AracDeleteDialog();
    expect(await aracDeleteDialog.getDialogTitle()).to.eq('otoBakimApp.arac.delete.question');
    await aracDeleteDialog.clickOnConfirmButton();

    expect(await aracComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
