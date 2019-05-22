/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { CariComponentsPage, CariDeleteDialog, CariUpdatePage } from './cari.page-object';

const expect = chai.expect;

describe('Cari e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let cariUpdatePage: CariUpdatePage;
  let cariComponentsPage: CariComponentsPage;
  let cariDeleteDialog: CariDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Caris', async () => {
    await navBarPage.goToEntity('cari');
    cariComponentsPage = new CariComponentsPage();
    await browser.wait(ec.visibilityOf(cariComponentsPage.title), 5000);
    expect(await cariComponentsPage.getTitle()).to.eq('otoBakimApp.cari.home.title');
  });

  it('should load create Cari page', async () => {
    await cariComponentsPage.clickOnCreateButton();
    cariUpdatePage = new CariUpdatePage();
    expect(await cariUpdatePage.getPageTitle()).to.eq('otoBakimApp.cari.home.createOrEditLabel');
    await cariUpdatePage.cancel();
  });

  it('should create and save Caris', async () => {
    const nbButtonsBeforeCreate = await cariComponentsPage.countDeleteButtons();

    await cariComponentsPage.clickOnCreateButton();
    await promise.all([
      cariUpdatePage.tipiSelectLastOption(),
      cariUpdatePage.kisiTipiSelectLastOption(),
      cariUpdatePage.setAdInput('ad'),
      cariUpdatePage.setAdresInput('adres'),
      cariUpdatePage.setTelefonInput('telefon'),
      cariUpdatePage.setTcNoInput('tcNo'),
      cariUpdatePage.setVergiNoInput('vergiNo'),
      cariUpdatePage.setYetkiliInput('yetkili'),
      cariUpdatePage.setFaxInput('fax'),
      cariUpdatePage.setEpostaInput('eposta'),
      cariUpdatePage.setWebAdresiInput('webAdresi'),
      cariUpdatePage.setIskontoInput('5'),
      cariUpdatePage.setAciklamaInput('aciklama'),
      cariUpdatePage.varsayilanIsEmriTipiSelectLastOption(),
      cariUpdatePage.hesapSelectLastOption()
    ]);
    const selectedAktif = cariUpdatePage.getAktifInput();
    if (await selectedAktif.isSelected()) {
      await cariUpdatePage.getAktifInput().click();
      expect(await cariUpdatePage.getAktifInput().isSelected(), 'Expected aktif not to be selected').to.be.false;
    } else {
      await cariUpdatePage.getAktifInput().click();
      expect(await cariUpdatePage.getAktifInput().isSelected(), 'Expected aktif to be selected').to.be.true;
    }
    expect(await cariUpdatePage.getAdInput()).to.eq('ad', 'Expected Ad value to be equals to ad');
    expect(await cariUpdatePage.getAdresInput()).to.eq('adres', 'Expected Adres value to be equals to adres');
    expect(await cariUpdatePage.getTelefonInput()).to.eq('telefon', 'Expected Telefon value to be equals to telefon');
    expect(await cariUpdatePage.getTcNoInput()).to.eq('tcNo', 'Expected TcNo value to be equals to tcNo');
    expect(await cariUpdatePage.getVergiNoInput()).to.eq('vergiNo', 'Expected VergiNo value to be equals to vergiNo');
    expect(await cariUpdatePage.getYetkiliInput()).to.eq('yetkili', 'Expected Yetkili value to be equals to yetkili');
    expect(await cariUpdatePage.getFaxInput()).to.eq('fax', 'Expected Fax value to be equals to fax');
    expect(await cariUpdatePage.getEpostaInput()).to.eq('eposta', 'Expected Eposta value to be equals to eposta');
    expect(await cariUpdatePage.getWebAdresiInput()).to.eq('webAdresi', 'Expected WebAdresi value to be equals to webAdresi');
    expect(await cariUpdatePage.getIskontoInput()).to.eq('5', 'Expected iskonto value to be equals to 5');
    const selectedEfaturaKullanimi = cariUpdatePage.getEfaturaKullanimiInput();
    if (await selectedEfaturaKullanimi.isSelected()) {
      await cariUpdatePage.getEfaturaKullanimiInput().click();
      expect(await cariUpdatePage.getEfaturaKullanimiInput().isSelected(), 'Expected efaturaKullanimi not to be selected').to.be.false;
    } else {
      await cariUpdatePage.getEfaturaKullanimiInput().click();
      expect(await cariUpdatePage.getEfaturaKullanimiInput().isSelected(), 'Expected efaturaKullanimi to be selected').to.be.true;
    }
    expect(await cariUpdatePage.getAciklamaInput()).to.eq('aciklama', 'Expected Aciklama value to be equals to aciklama');
    await cariUpdatePage.save();
    expect(await cariUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await cariComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Cari', async () => {
    const nbButtonsBeforeDelete = await cariComponentsPage.countDeleteButtons();
    await cariComponentsPage.clickOnLastDeleteButton();

    cariDeleteDialog = new CariDeleteDialog();
    expect(await cariDeleteDialog.getDialogTitle()).to.eq('otoBakimApp.cari.delete.question');
    await cariDeleteDialog.clickOnConfirmButton();

    expect(await cariComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
