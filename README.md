# isdialogmote-schema
AVRO schemas for application Isdialogmote

## Kafka Topic

### Topic Name:
teamsykefravr.isdialogmote-dialogmote-statusendring

### Properties
Description of the properties in the record schema:
- dialogmoteUuid: UUID of Dialogmote
- dialogmoteTidspunkt: The date-time a Dialogmote is held. Represented in ms from epoch.  
- statusEndringType: Type of StatusEndring. One of the following types:  INNKALT, AVLYST, FERDIGSTILT, NYTT_TID_STED.
- statusEndringTidspunkt: The date-time the StatusEndring was created. Represented in ms from epoch.
- personIdent: Identification number of Arbeidstaker(D-nummer or F-nummer)
- virksomhetsnummer: Virsomhetsnummer of the Arbeidsgiver.
- enhetNr: 4-digit number of the NAV-enhet that NavIdent represents.
- navIdent: Ident of the SYFO-veileder that initiated the event.
- tilfelleStartdato: Start date of the last Oppfolgingstilfelle at the given statusEndringTidspunkt. Represented in ms from epoch.
- arbeidstaker: Is Arbeidstaker Motedeltaker.
- arbeidsgiver: Is Arbeidsgiver Motedeltaker.
- sykmelder: Is Sykmelder Motedeltaker.
