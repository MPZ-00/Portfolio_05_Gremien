insert into Antrag (ID, Titel, Text, Ergebnis, Angenommen) values (4, 'Erweiterung des Parkplatzes', 'Der Parkplatz ist momentan immer voll und es wird Zeit, dass wir eine Erweiterung planen und umsetzen.', 'ja', '1');
insert into Antrag (ID, Titel, Text, Ergebnis, Angenommen) values (5, 'Einführung von flexibleren Arbeitszeiten', 'Um unsere Mitarbeiter zufriedener zu machen und ihnen mehr Freiheiten zu geben, möchten wir flexiblere Arbeitszeiten einführen.', 'Nein', '0');
insert into Antrag (ID, Titel, Text, Ergebnis, Angenommen) values (6, 'Erhöhung der Mitarbeiterbeteiligung an Projekten', 'Um die Motivation und das Engagement unserer Mitarbeiter zu erhöhen, möchten wir sie stärker in die Projekte einbinden und ihnen mehr Verantwortung übertragen.', 'Enthaltung', '0');

insert into Gremien (ID, Name, offiziell, inoffiziell, Beginn, Ende) values (4, 'Betriebsrat', '1', '0', to_date('01.01.2022', 'dd.mm.YYYY'), to_date('31.12.2022', 'dd.mm.YYYY'));
insert into Gremien (ID, Name, offiziell, inoffiziell, Beginn, Ende) values (5, 'Kundenfeedback-Gruppe', '0', '1', to_date('15.03.2021', 'dd.mm.YYYY'), to_date('14.03.2022', 'dd.mm.YYYY'));
insert into Gremien (ID, Name, offiziell, inoffiziell, Beginn, Ende) values (6, 'Arbeitskreis Digitalisierung', '1', '0', to_date('01.07.2021', 'dd.mm.YYYY'), to_date('30.06.2022', 'dd.mm.YYYY'));
insert into Gremien (ID, Name, offiziell, inoffiziell, Beginn, Ende) values (7, 'Mitarbeiter-Initiative', '0', '1', to_date('01.01.2022', 'dd.mm.YYYY'), to_date('31.12.2022', 'dd.mm.YYYY'));
insert into Gremien (ID, Name, offiziell, inoffiziell, Beginn, Ende) values (8, 'Geschäftsführung', '1', '0', to_date('01.01.2022', 'dd.mm.YYYY'), to_date('31.12.2022', 'dd.mm.YYYY'));

insert into Aufgabengebiete (ID, Ag_ID, Aufgabengebiet) values (7, 6, 'Qualitätskontrolle und sicherung');
insert into Aufgabengebiete (ID, Ag_ID, Aufgabengebiet) values (8, 4, 'IT-Support und Entwicklung');
insert into Aufgabengebiete (ID, Ag_ID, Aufgabengebiet) values (9, 7, 'Einkauf undBeschaffung');
insert into Aufgabengebiete (ID, Ag_ID, Aufgabengebiet) values (10, 1, 'Projektmanagement');
insert into Aufgabengebiete (ID, Ag_ID, Aufgabengebiet) values (11, 5, 'Kundenbetreuung');
insert into Aufgabengebiete (ID, Ag_ID, Aufgabengebiet) values (12, 1, 'Personalmanagement');
insert into Aufgabengebiete (ID, Ag_ID, Aufgabengebiet) values (13, 7, 'Marketing und Werbung');
insert into Aufgabengebiete (ID, Ag_ID, Aufgabengebiet) values (14, 2, 'Finanzplanung und kontrolle');
insert into Aufgabengebiete (ID, Ag_ID, Aufgabengebiet) values (15, 8, 'Produktentwicklung und management');
insert into Aufgabengebiete (ID, Ag_ID, Aufgabengebiet) values (16, 3, 'Lieferantenmanagement');

insert into Dokument (ID, Mime_Typ, Erstelldatum, Inhalt) values (5, 'pdf', to_date('15.01.2022', 'dd.mm.YYYY'), 'Inhalt des Dokuments');
insert into Dokument (ID, Mime_Typ, Erstelldatum, Inhalt) values (6, 'docx', to_date('01.03.2022', 'dd.mm.YYYY'), 'Inhalt des Dokuments');
insert into Dokument (ID, Mime_Typ, Erstelldatum, Inhalt) values (7, 'xlsx', to_date('22.05.2022', 'dd.mm.YYYY'), 'Inhalt des Dokuments');
insert into Dokument (ID, Mime_Typ, Erstelldatum, Inhalt) values (8, 'png', to_date('08.07.2022', 'dd.mm.YYYY'), 'Inhalt des Dokuments');
insert into Dokument (ID, Mime_Typ, Erstelldatum, Inhalt) values (9, 'jpeg', to_date('19.09.2022', 'dd.mm.YYYY'), 'Inhalt des Dokuments');
insert into Dokument (ID, Mime_Typ, Erstelldatum, Inhalt) values (10, 'txt', to_date('30.11.2022', 'dd.mm.YYYY'), 'Inhalt des Dokuments');

insert into Personen (ID, Geburtsdatum, Geschlecht) values (5, to_date('01.01.2000', 'dd.mm.YYYY'), 'm');
insert into Namen (ID, Vorname, Nachname) values (5, 'Max', 'Mustermann');
insert into Adresse (ID, Strasse, Hausnummer, PLZ, Ort) values (5, 'Musterstr.', 25, 12345, 'Musterstadt');
insert into Professoren (ID) values (5);

insert into Personen (ID, Geburtsdatum, Geschlecht) values (6, to_date('15.03.2005', 'dd.mm.YYYY'), 'w');
insert into Namen (ID, Vorname, Nachname) values (6, 'Lisa', 'Lustig');
insert into Adresse (ID, Strasse, Hausnummer, PLZ, Ort) values (6, 'Lustigstr.', 75, 54321, 'Lustighausen');
insert into Lehrbeauftragte (ID) values (6);

insert into Personen (ID, Geburtsdatum, Geschlecht) values (7, to_date('01.07.2000', 'dd.mm.YYYY'), 'd');
insert into Namen (ID, Vorname, Nachname) (7, 'Friedrich', 'Feuerstein');
insert into Adresse (ID, Strasse, Hausnummer, PLZ, Ort) values (7, 'Feuersteinstr.', 10, 98765, 'Feuersteinburg');
insert into Mitarbeiter (ID) values (7);

insert into Personen (ID, Geburtsdatum, Geschlecht) values (8, to_date('31.12.1999', 'dd.mm.YYYY'), 'w');
insert into Namen (ID, Vorname, Nachname) values (8, 'Sara', 'Scharf');
insert into Adresse (ID, Strasse, Hausnummer, PLZ, Ort) values (8, 'Scharfstr.', 30, 13579, 'Scharfenberg');
insert into Sonstige_Personen (ID) values (8);

insert into erstellt_von (ID_Author, ID_Dokument) values (5, 8);
insert into erstellt_von (ID_Author, ID_Dokument) values (6, 10);
insert into erstellt_von (ID_Author, ID_Dokument) values (7, 5);
insert into erstellt_von (ID_Author, ID_Dokument) values (8, 6);
insert into erstellt_von (ID_Author, ID_Dokument) values (5, 7);
insert into erstellt_von (ID_Author, ID_Dokument) values (6, 9);

insert into Tagesordnung (ID, Titel, Kurzbeschreibung, Protokolltext) values (5, 'Bauprojekt Parkplatz', 'Besprechung des Bauprojekts des neuen Parkplatzes', 'Im Protokoll wird besprochen, welche Firma den Zuschlag erhält und wann mit dem Bau begonnen wird.');
insert into Tagesordnung (ID, Titel, Kurzbeschreibung, Protokolltext) values (6, 'Einführung neues CRM-System', 'Besprechung der Einführung des neuen CRM-Systems im Unternehmen', 'Im Protokoll wird diskutiert, welches CRM-System am besten geeignet ist und wann die Einführung stattfinden soll.');
insert into Tagesordnung (ID, Titel, Kurzbeschreibung, Protokolltext) values (7, 'Umstrukturierung der Abteilungen', 'Besprechung der Umstrukturierung der Abteilungen im Unternehmen', 'Im Protokoll werden die neuen Strukturen besprochen und entschieden, welche Abteilungen neu geschaffen oder zusammengelegt werden sollen.');
insert into Tagesordnung (ID, Titel, Kurzbeschreibung, Protokolltext) values (8, 'Mitarbeiter-Feedback-Umfrage', 'Besprechung der Ergebnisse der Mitarbeiter-Feedback-Umfrage', 'Im Protokoll werden die Ergebnisse der Umfrage besprochen und Maßnahmen festgelegt, um die Zufriedenheit der Mitarbeiter zu erhöhen.');
insert into Tagesordnung (ID, Titel, Kurzbeschreibung, Protokolltext) values (9, 'Marketingstrategie 2022', 'Besprechung der Marketingstrategie für das Jahr 2022', 'Im Protokoll wird die Marketingstrategie für das kommende Jahr besprochen und entschieden, welche Maßnahmen umgesetzt werden sollen.');
insert into Tagesordnung (ID, Titel, Kurzbeschreibung, Protokolltext) values (10, 'Budgetplanung 2022', 'Besprechung der Budgetplanung für das Jahr 2022', 'Im Protokoll wird die Budgetplanung für das kommende Jahr besprochen und entschieden, wie das vorhandene Budget am besten eingesetzt werden soll.');
insert into Tagesordnung (ID, Titel, Kurzbeschreibung, Protokolltext) values (11, 'Kooperation mit neuem Partnerunternehmen', 'Besprechung der Kooperation mit dem neuen Partnerunternehmen', 'Im Protokoll wird besprochen, welche Vorteile die Kooperation für beide Unternehmen bietet und wie die Zusammenarbeit gestaltet werden soll.');
insert into Tagesordnung (ID, Titel, Kurzbeschreibung, Protokolltext) values (12, 'Einführung neuer Arbeitszeitmodelle', 'Besprechung der Einführung von neuen Arbeitszeitmodellen im Unternehmen', 'Im Protokoll werden die verschiedenen Arbeitszeitmodelle vorgestellt und entschieden, welche Modelle eingeführt werden sollen.');
insert into Tagesordnung (ID, Titel, Kurzbeschreibung, Protokolltext) values (13, 'Beschaffung neuer IT-Hardware', 'Besprechung der Beschaffung neuer IT-Hardware für das Unternehmen', 'Im Protokoll wird besprochen, welche Hardware benötigt wird und welche Anbieter in Betracht kommen.');
insert into Tagesordnung (ID, Titel, Kurzbeschreibung, Protokolltext) values (14, 'Weiterentwicklung der Unternehmensziele', 'Besprechung der Weiterentwicklung der Unternehmensziele', 'Im Protokoll werden die aktuellen Ziele besprochen und entschieden, welche Ziele im kommenden Jahr verfolgt werden sollen.');
insert into Tagesordnung (ID, Titel, Kurzbeschreibung, Protokolltext) values (15, 'Einführung eines neuen Qualitätsmanagementsystems', 'Besprechung der Einführung eines neuen Qualitätsmanagementsystems im Unternehmen', 'Im Protokoll wird besprochen, welches Qualitätsmanagementsystem am besten geeignet ist und wann die Einführung stattfinden soll.');
insert into Tagesordnung (ID, Titel, Kurzbeschreibung, Protokolltext) values (16, 'Erweiterung des Teams', 'Besprechung der Erweiterung des Teams im Unternehmen', 'Im Protokoll wird besprochen, wie viele neue Mitarbeiter benötigt werden und welche Stellen ausgeschrieben werden sollen.');

insert into Sitzungen (ID, Beginn, Ende, Einladung_am, oeffentlich, Ort, Protokoll) values (4, to_timestamp('04.05.2022 15:00:00', 'dd.mm.YYYY HH24:MI:SS'), to_timestamp('04.05.2022 17:00:00', 'dd.mm.YYYY HH24:MI:SS'), to_date('03.05.2022', 'dd.mm.YYYY'), '0', 'Besprechungsraum 1', 'Protokoll der Sitzung vom 04.05.2022');
insert into Sitzungen (ID, Beginn, Ende, Einladung_am, oeffentlich, Ort, Protokoll) values (5, to_timestamp('11.05.2022 10:00:00', 'dd.mm.YYYY HH24:MI:SS'), to_timestamp('11.05.2022 12:00:00', 'dd.mm.YYYY HH24:MI:SS'), to_date('10.05.2022', 'dd.mm.YYYY'), '1', 'Hauptsitzungsraum', 'Protokoll der Sitzung vom 11.05.2022');
insert into Sitzungen (ID, Beginn, Ende, Einladung_am, oeffentlich, Ort, Protokoll) values (6, to_timestamp('18.05.2022 09:00:00', 'dd.mm.YYYY HH24:MI:SS'), to_timestamp('18.05.2022 11:00:00', 'dd.mm.YYYY HH24:MI:SS'), to_date('17.05.2022', 'dd.mm.YYYY'), '0', 'Konferenzraum 2', 'Protokoll der Sitzung vom 18.05.2022');
insert into Sitzungen (ID, Beginn, Ende, Einladung_am, oeffentlich, Ort, Protokoll) values (7, to_timestamp('25.05.2022 14:00:00', 'dd.mm.YYYY HH24:MI:SS'), to_timestamp('25.05.2022 16:00:00', 'dd.mm.YYYY HH24:MI:SS'), to_date('24.05.2022', 'dd.mm.YYYY'), '1', 'Hauptsitzungsraum', 'Protokoll der Sitzung vom 25.05.2022');
insert into Sitzungen (ID, Beginn, Ende, Einladung_am, oeffentlich, Ort, Protokoll) values (8, to_timestamp('01.06.2022 13:00:00', 'dd.mm.YYYY HH24:MI:SS'), to_timestamp('01.06.2022 15:00:00', 'dd.mm.YYYY HH24:MI:SS'), to_date('31.05.2022', 'dd.mm.YYYY'), '0', 'Besprechungsraum 1', 'Protokoll der Sitzung vom 01.06.2022');

insert into top (ID_Sitzung, ID_Tagesordnung) values (4, 5);
insert into top (ID_Sitzung, ID_Tagesordnung) values (4, 6);
insert into top (ID_Sitzung, ID_Tagesordnung) values (5, 7);
insert into top (ID_Sitzung, ID_Tagesordnung) values (5, 8);
insert into top (ID_Sitzung, ID_Tagesordnung) values (6, 9);
insert into top (ID_Sitzung, ID_Tagesordnung) values (6, 10);
insert into top (ID_Sitzung, ID_Tagesordnung) values (7, 11);
insert into top (ID_Sitzung, ID_Tagesordnung) values (7, 12);
insert into top (ID_Sitzung, ID_Tagesordnung) values (8, 13);
insert into top (ID_Sitzung, ID_Tagesordnung) values (8, 14);
insert into top (ID_Sitzung, ID_Tagesordnung) values (3, 15);
insert into top (ID_Sitzung, ID_Tagesordnung) values (3, 16);