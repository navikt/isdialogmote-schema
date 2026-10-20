package no.nav.syfo.dialogmote.avro;

import org.apache.avro.Schema;
import org.apache.avro.SchemaCompatibility;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

import static org.apache.avro.SchemaCompatibility.SchemaCompatibilityType.COMPATIBLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class KDialogmoteStatusEndringCompatibilityTest {

    private static final Schema OLD_SCHEMA = loadSchema("/avro/dialogmote_status_endring_1.0.5.avsc");
    private static final Schema NEW_SCHEMA = KDialogmoteStatusEndring.getClassSchema();

    @Test
    void newSchemaCanReadDataWrittenWithOldSchema() {
        assertEquals(
            COMPATIBLE,
            SchemaCompatibility.checkReaderWriterCompatibility(NEW_SCHEMA, OLD_SCHEMA).getType()
        );
    }

    @Test
    void oldSchemaCanReadDataWrittenWithNewSchema() {
        assertEquals(
            COMPATIBLE,
            SchemaCompatibility.checkReaderWriterCompatibility(OLD_SCHEMA, NEW_SCHEMA).getType()
        );
    }

    @Test
    void builderDefaultsEndringerToNull() {
        assertNull(newBuilder().build().getEndringer());
    }

    @Test
    void recordWrittenWithNewSchemaIsReadableWithOldSchema() throws IOException {
        KDialogmoteStatusEndring record = newBuilder()
            .setStatusEndringType("ENDRET")
            .setEndringer(Arrays.asList("TID_STED", "BEHANDLER_LAGT_TIL"))
            .build();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(out, null);
        new SpecificDatumWriter<>(KDialogmoteStatusEndring.class).write(record, encoder);
        encoder.flush();

        BinaryDecoder decoder = DecoderFactory.get().binaryDecoder(out.toByteArray(), null);
        GenericRecord read = new GenericDatumReader<GenericRecord>(NEW_SCHEMA, OLD_SCHEMA).read(null, decoder);

        assertEquals("ENDRET", read.get("statusEndringType").toString());
        assertNull(read.getSchema().getField("endringer"));
    }

    @Test
    void recordWrittenWithOldSchemaIsReadableWithNewSchema() throws IOException {
        GenericRecord record = new GenericData.Record(OLD_SCHEMA);
        record.put("dialogmoteUuid", UUID.randomUUID().toString());
        record.put("dialogmoteTidspunkt", 0L);
        record.put("statusEndringType", "INNKALT");
        record.put("statusEndringTidspunkt", 0L);
        record.put("personIdent", "12345678910");
        record.put("virksomhetsnummer", "123456789");
        record.put("enhetNr", "0101");
        record.put("navIdent", "Z999999");
        record.put("tilfelleStartdato", 0L);
        record.put("arbeidstaker", true);
        record.put("arbeidsgiver", true);
        record.put("sykmelder", false);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(out, null);
        new GenericDatumWriter<GenericRecord>(OLD_SCHEMA).write(record, encoder);
        encoder.flush();

        BinaryDecoder decoder = DecoderFactory.get().binaryDecoder(out.toByteArray(), null);
        KDialogmoteStatusEndring read =
            new SpecificDatumReader<KDialogmoteStatusEndring>(OLD_SCHEMA, NEW_SCHEMA).read(null, decoder);

        assertEquals("INNKALT", read.getStatusEndringType());
        assertNull(read.getEndringer());
    }

    private static KDialogmoteStatusEndring.Builder newBuilder() {
        Instant now = Instant.ofEpochMilli(System.currentTimeMillis());
        return KDialogmoteStatusEndring.newBuilder()
            .setDialogmoteUuid(UUID.randomUUID().toString())
            .setDialogmoteTidspunkt(now)
            .setStatusEndringType("INNKALT")
            .setStatusEndringTidspunkt(now)
            .setPersonIdent("12345678910")
            .setVirksomhetsnummer("123456789")
            .setEnhetNr("0101")
            .setNavIdent("Z999999")
            .setTilfelleStartdato(now)
            .setArbeidstaker(true)
            .setArbeidsgiver(true)
            .setSykmelder(false);
    }

    private static Schema loadSchema(String path) {
        try (InputStream in = KDialogmoteStatusEndringCompatibilityTest.class.getResourceAsStream(path)) {
            return new Schema.Parser().parse(in);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
