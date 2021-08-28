describe('Home Page', () => {
  it('login', () => {
    cy.visit('/'); // go to the home page

    // type login credentials and check if login is successful
    cy.get('input[name="username"]').type('ece');
    cy.get('input[name="password"]').type('test123')
    cy.get('input[type="submit"]').click();
    cy.url().should("equal","http://localhost:4200/u/prg")
  });

  it('create new program', () => {
    cy.visit('/u/createProgram');

    cy.get('input[name="name"]').type('New Program 1');
    cy.get('p-editor').type('This is New Program');
    cy.get('.nxt-btn').first().click();
    cy.get('.nxt-btn').last().click();

    cy.visit('/u/prg');
    cy.contains('New Program 1')
  });

  it('create new task', () => {
    cy.visit('/u/prg');
    cy.get('a').first().click()
    cy.visit('/t/taskCreate')
    cy.get('input[name="name"]').type('Task 1');
    cy.get('p-editor').type('This is New Task');
    cy.get('.migrateBtn').contains('Next').first().click();
    cy.get('#start').type('2021-11-01T08:30');
    cy.get('#deadline').type('2021-12-01T08:30');
    cy.get('.ng-star-inserted').contains('3').first().click();
    cy.get('.ng-star-inserted').contains('4').first().click();
    cy.get('.mat-button-wrapper').contains('PUBLISH TASK').click();
  });

  it('logout', () =>{
    cy.get('.topright').contains('input').click()
    cy.url().should('contain','login')
  })
});
