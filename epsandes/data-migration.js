// Data Migration Script from Oracle to MongoDB
// This script provides a template for migrating data from Oracle EPSAndes to MongoDB

use('epsandes');

// Sample data insertion commands (replace with actual data from Oracle)

// Migrate Afiliados
print("Migrating Afiliados...");
// Example structure - replace with actual Oracle data
/*
db.afiliados.insertMany([
    {
        numeroDocumento: NumberLong(1234567890),
        tipoDocumento: "CC",
        nombre: "Juan",
        apellido: "Pérez",
        fechaNacimiento: new Date("1990-01-15"),
        fechaAfiliacion: new Date("2020-01-01"),
        contribuyenteNumeroDocumento: NumberLong(987654321)
    }
    // Add more afiliados based on Oracle data
]);
*/

// Migrate IPS
print("Migrating IPS...");
// Transform Oracle IPS data to MongoDB format with embedded serviciosIds
/*
db.ips.insertMany([
    {
        nit: NumberLong(900123456),
        nombre: "Hospital Central",
        nivel: "PRIMARIO",
        ubicacion: "Bogotá",
        serviciosIds: [NumberLong(1), NumberLong(2), NumberLong(3)] // IDs of services provided
    }
    // Add more IPS based on Oracle data
]);
*/

// Migrate Servicios de Salud
print("Migrating Servicios de Salud...");
/*
db.serviciosDeSalud.insertMany([
    {
        idServicio: NumberLong(1),
        nombre: "Consulta General",
        tipo: "CONSULTA_GENERAL",
        costo: 50000.0
    },
    {
        idServicio: NumberLong(2),
        nombre: "Consulta Especializada",
        tipo: "CONSULTA_ESPECIALIZADA",
        costo: 80000.0
    }
    // Add more services based on Oracle data
]);
*/

// Migrate Medicos
print("Migrating Medicos...");
// Transform Oracle Medicos data to include reference IDs instead of relationships
/*
db.medicos.insertMany([
    {
        numeroDocumento: NumberLong(1122334455),
        nombre: "Dr. Carlos",
        apellido: "García",
        especialidad: "MEDICINA_GENERAL",
        ipsNit: NumberLong(900123456), // Reference to IPS
        serviciosIds: [NumberLong(1), NumberLong(2)] // Services this medico provides
    }
    // Add more medicos based on Oracle data
]);
*/

// Migrate Ordenes de Servicio
print("Migrating Ordenes de Servicio...");
// Transform Oracle OrdenDeServicio data to use reference IDs
/*
db.ordenesDeServicio.insertMany([
    {
        idOrden: NumberLong(1),
        fechaHora: new Date("2025-01-15T10:00:00Z"),
        estadoOrden: "VIGENTE",
        medicoNumeroDocumento: NumberLong(1122334455), // Reference to Medico
        afiliadoNumeroDocumento: NumberLong(1234567890), // Reference to Afiliado
        serviciosIds: [NumberLong(2), NumberLong(3)] // Services in this order
    }
    // Add more orders based on Oracle data
]);
*/

// Migrate Agendar Citas
print("Migrating Agendar Citas...");
// Transform Oracle AgendarCita data to use reference IDs
/*
db.agendarCitas.insertMany([
    {
        idCita: NumberLong(1),
        fechaHora: new Date("2025-01-15T10:00:00Z"),
        afiliadoNumeroDocumento: NumberLong(1234567890), // Reference to Afiliado
        medicoNumeroDocumento: NumberLong(1122334455), // Reference to Medico
        servicioSaludId: NumberLong(1), // Reference to ServicioDeSalud
        idOrdenDeServicio: NumberLong(1) // Reference to OrdenDeServicio (can be null)
    }
    // Add more citas based on Oracle data
]);
*/

// Migrate Prestacion Servicios
print("Migrating Prestacion Servicios...");
// Transform Oracle PrestacionServicio data with new ID strategy
/*
db.prestacionServicios.insertMany([
    {
        agendarCitaId: NumberLong(1), // Reference to AgendarCita
        ipsNit: NumberLong(900123456), // Reference to IPS
        citaRealizada: true
    }
    // Add more prestaciones based on Oracle data
]);
*/

// Data validation queries to check migration
print("Running validation queries...");

print("Afiliados count: " + db.afiliados.countDocuments());
print("IPS count: " + db.ips.countDocuments());
print("Servicios de Salud count: " + db.serviciosDeSalud.countDocuments());
print("Medicos count: " + db.medicos.countDocuments());
print("Ordenes de Servicio count: " + db.ordenesDeServicio.countDocuments());
print("Agendar Citas count: " + db.agendarCitas.countDocuments());
print("Prestacion Servicios count: " + db.prestacionServicios.countDocuments());

// Sample validation queries
print("\nSample validation queries:");

// Check if all medicos reference valid IPS
print("Medicos with invalid IPS references:");
db.medicos.aggregate([
    {
        $lookup: {
            from: "ips",
            localField: "ipsNit",
            foreignField: "nit",
            as: "ipsMatch"
        }
    },
    {
        $match: {
            ipsMatch: { $size: 0 }
        }
    },
    {
        $project: {
            numeroDocumento: 1,
            nombre: 1,
            apellido: 1,
            ipsNit: 1
        }
    }
]);

// Check if all citas reference valid afiliados, medicos, and servicios
print("Citas with invalid references:");
db.agendarCitas.aggregate([
    {
        $lookup: {
            from: "afiliados",
            localField: "afiliadoNumeroDocumento",
            foreignField: "numeroDocumento",
            as: "afiliadoMatch"
        }
    },
    {
        $lookup: {
            from: "medicos",
            localField: "medicoNumeroDocumento",
            foreignField: "numeroDocumento",
            as: "medicoMatch"
        }
    },
    {
        $lookup: {
            from: "serviciosDeSalud",
            localField: "servicioSaludId",
            foreignField: "idServicio",
            as: "servicioMatch"
        }
    },
    {
        $match: {
            $or: [
                { afiliadoMatch: { $size: 0 } },
                { medicoMatch: { $size: 0 } },
                { servicioMatch: { $size: 0 } }
            ]
        }
    },
    {
        $project: {
            idCita: 1,
            afiliadoNumeroDocumento: 1,
            medicoNumeroDocumento: 1,
            servicioSaludId: 1,
            afiliadoValid: { $gt: [{ $size: "$afiliadoMatch" }, 0] },
            medicoValid: { $gt: [{ $size: "$medicoMatch" }, 0] },
            servicioValid: { $gt: [{ $size: "$servicioMatch" }, 0] }
        }
    }
]);

print("Data migration template completed!");
print("To complete the migration:");
print("1. Export data from Oracle database");
print("2. Transform the data according to the new MongoDB schema");
print("3. Replace the commented insertMany calls with actual data");
print("4. Run this script to insert the transformed data");
print("5. Validate the migration using the provided queries");
