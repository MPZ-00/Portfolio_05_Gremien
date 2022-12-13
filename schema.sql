create table if not exists Gremien (
    ID integer primary key,
    Name varchar (50),
    offiziell varchar (1) not null,
    inoffiziell varchar (1) not null,
    Beginn date,
    Ende date
);

create table if not exists Aufgabengebiete (
    ID integer primary key,
    Ag_ID integer,
    Aufgabengebiet varchar (100),
    constraint fk_Ag_Gremien foreign key (Ag_ID) references Gremien (ID)
);

create table if not exists Personen (
    ID integer primary key,
    Geburtsdatum date,
    Geschlecht char (1)
);

create table if not exists Namen (
    ID integer primary key,
    Vorname varchar (100),
    Nachname varchar (300),
    constraint fk_Namen_Personen foreign key (ID) references Personen (ID)
);

create table if not exists Adresse (
    ID integer primary key,
    Strasse varchar (200),
    Hausnummer integer,
    PLZ integer,
    Ort varchar (100),
    constraint fk_Adresse_Personen foreign key (ID) references Personen (ID)
);

create table if not exists Sitzungen (
    ID integer primary key,
    Beginn timestamp,
    Ende timestamp,
    Einladung_am date,
    oeffentlich varchar (1), /* da boolean nicht geht, varchar (1) not null */
    Ort varchar (100),
    Protokoll varchar (4000)
);

create table if not exists Tagesordnung (
    ID integer primary key,
    Titel varchar (100),
    Kurzbeschreibung varchar (500),
    Protokolltext varchar (4000)
);

create table if not exists Lehrbeauftragte (
    ID integer primary key,
    constraint fk_Lehrbeauftragte_Pers foreign key (ID) references Personen (ID)
);

create table if not exists Mitarbeiter (
    ID integer primary key,
    constraint fk_Mitarbeiter_Pers foreign key (ID) references Personen (ID)
);

create table if not exists Professoren (
    ID integer primary key,
    constraint fk_Professoren_Pers foreign key (ID) references Personen (ID)
);

create table if not exists Student (
    ID integer primary key,
    Studiengang varchar (100),
    Studienbeginn date,
    MatrikelNr integer,
    constraint fk_Student_Pers foreign key (ID) references Personen (ID)
);

create table if not exists Sonstige_Personen (
    ID integer primary key,
    constraint fk_Sonstige_Pers foreign key (ID) references Personen (ID)
);

create table if not exists Dokument (
    ID integer primary key,
    Mime_Typ varchar (50),
    Erstelldatum date,
    Inhalt varchar (4000)
    /* Dokument Author wurde entfernt, da der Author in 'erstellt_von' benannt wird */
);

create table if not exists Antrag (
    ID integer primary key,
    Titel varchar (100),
    Text varchar (300),
    Ergebnis varchar (50), /* 'ja', 'nein', 'enthaltung' */
    Angenommen varchar (1) not null /* da boolean nicht geht, varchar (1) not null */
);

create table if not exists nimmt_teil (
    ID_Personen integer,
    ID_Sitzungen integer,
    constraint fk_nt_Person foreign key (ID_Personen) references Personen (ID),
    constraint fk_nt_Sitzung foreign key (ID_Sitzungen) references Sitzungen (ID),
    constarint pk_nt primary key (ID_Personen, ID_Sitzungen)
);

create table if not exists fuehrt_Protokoll_bei (
    ID_Personen integer,
    ID_Sitzungen integer,
    constraint fk_fPb_Person foreign key (ID_Personen) references Personen (ID),
    constraint fk_fPb_Sitzung foreign key (ID_Sitzungen) references Sitzungen (ID),
    constraint pk_fPb_ID primary key (ID_Personen, ID_Sitzungen)
);

create table if not exists top (
    ID_Sitzung integer,
    ID_Tagesordnung integer,
    constraint fk_top_Sitzungen foreign key (ID_Sitzung) references Sitzungen (ID),
    constraint fk_top_Tagesordnung foreign key (ID_Tagesordnung) references Tagesordnung (ID),
    constraint pk_top_ID primary key (ID_Sitzung, ID_Tagesordnung)
);

create table if not exists hat (
    ID_Gremien integer,
    ID_Sitzungen integer,
    constraint fk_hat_Gremien foreign key (ID_Gremien) references Gremien (ID),
    constraint fk_hat_Sitzungen foreign key (ID_Sitzungen) references Sitzungen (ID) on delete set null,
    constraint pk_hat_ID primary key (ID_Gremien, ID_Sitzungen)
);

create table if not exists erstellt_von (
    ID_Author integer primary key,
    ID_Dokument integer,
    constraint fk_ert_Personen foreign key (ID_Author) references Personen (ID) on delete set null,
    constraint fk_ert_Dokument foreign key (ID_Dokument) references Dokument (ID) on delete set null
);

create table if not exists Mitglieder (
    ID_Gremien integer,
    ID_Personen integer,
    Funktion varchar (200),
    constraint fk_Mitglieder_Gremien foreign key (ID_Gremien) references Gremien (ID),
    constraint fk_Mitglieder_Personen foreign key (ID_Personen) references Personen (ID),
    constraint pk_Mitglieder_ID primary key (ID_Gremien, ID_Personen)
);

create table if not exists stellt (
    ID_Person integer,
    ID_Antrag integer,
    constraint fk_stellt_Person foreign key (ID_Person) references Personen (ID) on delete set null,
    constraint fk_stellt_Antrag foreign key (ID_Antrag) references Antrag (ID) on delete set null,
    constraint pk_stellt_ID primary key (ID_Person, ID_Antrag)
);

create table if not exists gehoert_zu (
    ID_Antrag integer,
    ID_TOP integer,
    constraint fk_gehoert_zu_Antrag foreign key (ID_Antrag) references Antrag (ID) on delete set null,
    constraint fk_gehoert_zu_TOP foreign key (ID_TOP) references Tagesordnung (ID) on delete set null,
    constraint pk_gehoert_zu_ID primary key (ID_Antrag, ID_TOP)
);

create table if not exists if not exists fuer (
    ID_Dokument integer,
    ID_TOP integer,
    constraint fk_fuer_Dokument foreign key (ID_Dokument) references Dokument (ID),
    constraint fk_fuer_TOP foreign key (ID_TOP) references Tagesordnung (ID),
    constraint pk_fuer primary key (ID_Dokument, ID_TOP)
);