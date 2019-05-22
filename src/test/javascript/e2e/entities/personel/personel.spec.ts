/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { PersonelComponentsPage, PersonelDeleteDialog, PersonelUpdatePage } from './personel.page-object';

const expect = chai.expect;

describe('Personel e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let personelUpdatePage: PersonelUpdatePage;
  let personelComponentsPage: PersonelComponentsPage;
  let personelDeleteDialog: PersonelDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Personels', async () => {
    await navBarPage.goToEntity('personel');
    personelComponentsPage = new PersonelComponentsPage();
    await browser.wait(ec.visibilityOf(personelComponentsPage.title), 5000);
    expect(await personelComponentsPage.getTitle()).to.eq('otoBakimApp.personel.home.title');
  });

  it('should load create Personel page', async () => {
    await personelComponentsPage.clickOnCreateButton();
    personelUpdatePage = new PersonelUpdatePage();
    expect(await personelUpdatePage.getPageTitle()).to.eq('otoBakimApp.personel.home.createOrEditLabel');
    await personelUpdatePage.cancel();
  });

  it('should create and save Personels', async () => {
    const nbButtonsBeforeCreate = await personelComponentsPage.countDeleteButtons();

    await personelComponentsPage.clickOnCreateButton();
    await promise.all([
      personelUpdatePage.setAdInput('ad'),
      personelUpdatePage.setSoyadInput('soyad'),
      personelUpdatePage.setGoreviInput('gorevi')
    ]);
    expect(await personelUpdatePage.getAdInput()).to.eq('ad', 'Expected Ad value to be equals to ad');
    expect(await personelUpdatePage.getSoyadInput()).to.eq('soyad', 'Expected Soyad value to be equals to soyad');
    expect(await personelUpdatePage.getGoreviInput()).to.eq('gorevi', 'Expected Gorevi value to be equals to gorevi');
    await personelUpdatePage.save();
    expect(await personelUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await personelComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Personel', async () => {
    const nbButtonsBeforeDelete = await personelComponentsPage.countDeleteButtons();
    await personelComponentsPage.clickOnLastDeleteButton();

    personelDeleteDialog = new PersonelDeleteDialog();
    expect(await personelDeleteDialog.getDialogTitle()).to.eq('otoBakimApp.personel.delete.question');
    await personelDeleteDialog.clickOnConfirmButton();

    expect(await personelComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
