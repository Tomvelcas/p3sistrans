// MongoDB Setup Script for EPSAndes
// Run this script in MongoDB shell to create collections and indexes

// Switch to epsandes database
use('epsandes');

// Create collections with validation schemas

// Afiliados collection
db.createCollection("afiliados", {
   validator: {
      $jsonSchema: {
         bsonType: "object",
         required: ["numeroDocumento", "tipoDocumento", "nombre", "apellido", "fechaNacimiento", "fechaAfiliacion", "contribuyenteNumeroDocumento"],
         properties: {
            numeroDocumento: {
               bsonType: "long",
               description: "must be a long and is required"
            },
            tipoDocumento: {
               bsonType: "string",
               description: "must be a string and is required"
            },
            nombre: {
               bsonType: "string",
               description: "must be a string and is required"
            },
            apellido: {
               bsonType: "string",
               description: "must be a string and is required"
            },
            fechaNacimiento: {
               bsonType: "date",
               description: "must be a date and is required"
            },
            fechaAfiliacion: {
               bsonType: "date",
               description: "must be a date and is required"
            },
            contribuyenteNumeroDocumento: {
               bsonType: "long",
               description: "must be a long and is required"
            }
         }
      }
   }
});

// IPS collection
db.createCollection("ips", {
   validator: {
      $jsonSchema: {
         bsonType: "object",
         required: ["nit", "nombre", "nivel", "ubicacion"],
         properties: {
            nit: {
               bsonType: "long",
               description: "must be a long and is required"
            },
            nombre: {
               bsonType: "string",
               description: "must be a string and is required"
            },
            nivel: {
               bsonType: "string",
               description: "must be a string and is required"
            },
            ubicacion: {
               bsonType: "string",
               description: "must be a string and is required"
            },
            serviciosIds: {
               bsonType: "array",
               items: {
                  bsonType: "long"
               },
               description: "must be an array of service IDs"
            }
         }
      }
   }
});

// Medicos collection
db.createCollection("medicos", {
   validator: {
      $jsonSchema: {
         bsonType: "object",
         required: ["numeroDocumento", "nombre", "apellido", "especialidad", "ipsNit"],
         properties: {
            numeroDocumento: {
               bsonType: "long",
               description: "must be a long and is required"
            },
            nombre: {
               bsonType: "string",
               description: "must be a string and is required"
            },
            apellido: {
               bsonType: "string",
               description: "must be a string and is required"
            },
            especialidad: {
               bsonType: "string",
               description: "must be a string and is required"
            },
            ipsNit: {
               bsonType: "long",
               description: "must be a long and is required"
            },
            serviciosIds: {
               bsonType: "array",
               items: {
                  bsonType: "long"
               },
               description: "must be an array of service IDs"
            }
         }
      }
   }
});

// EPS collection
db.createCollection("eps", {
   validator: {
      $jsonSchema: {
         bsonType: "object",
         required: ["nit", "nombre", "tipoEPS"],
         properties: {
            nit: {
               bsonType: "long",
               description: "must be a long and is required"
            },
            nombre: {
               bsonType: "string",
               description: "must be a string and is required"
            },
            telefono: {
               bsonType: "string",
               description: "EPS phone number"
            },
            direccion: {
               bsonType: "string",
               description: "EPS address"
            },
            email: {
               bsonType: "string",
               description: "EPS email"
            },
            tipoEPS: {
               bsonType: "string",
               description: "must be a string and is required"
            },
            ipsIds: {
               bsonType: "array",
               items: {
                  bsonType: "long"
               },
               description: "must be an array of IPS IDs"
            }
         }
      }
   }
});

// Servicios de Salud collection
db.createCollection("servicios_salud", {
   validator: {
      $jsonSchema: {
         bsonType: "object",
         required: ["idServicio", "nombre", "tipo"],
         properties: {
            idServicio: {
               bsonType: "long",
               description: "must be a long and is required"
            },
            nombre: {
               bsonType: "string",
               description: "must be a string and is required"
            },
            tipo: {
               bsonType: "string",
               description: "must be a string and is required"
            },
            descripcion: {
               bsonType: "string",
               description: "service description"
            }
         }
      }
   }
});

// Ordenes de Servicio collection
db.createCollection("ordenes_servicio", {
   validator: {
      $jsonSchema: {
         bsonType: "object",
         required: ["idOrden", "fechaHora", "estadoOrden", "medicoNumeroDocumento", "afiliadoNumeroDocumento"],
         properties: {
            idOrden: {
               bsonType: "long",
               description: "must be a long and is required"
            },
            fechaHora: {
               bsonType: "date",
               description: "must be a date and is required"
            },
            estadoOrden: {
               bsonType: "string",
               description: "must be a string and is required"
            },
            medicoNumeroDocumento: {
               bsonType: "long",
               description: "must be a long and is required"
            },
            afiliadoNumeroDocumento: {
               bsonType: "long",
               description: "must be a long and is required"
            },
            serviciosIds: {
               bsonType: "array",
               items: {
                  bsonType: "long"
               },
               description: "must be an array of service IDs"
            }
         }
      }
   }
});

// Agendar Citas collection
db.createCollection("citas", {
   validator: {
      $jsonSchema: {
         bsonType: "object",
         required: ["idCita", "fechaHora", "afiliadoNumeroDocumento", "medicoNumeroDocumento", "servicioSaludId"],
         properties: {
            idCita: {
               bsonType: "long",
               description: "must be a long and is required"
            },
            fechaHora: {
               bsonType: "date",
               description: "must be a date and is required"
            },
            afiliadoNumeroDocumento: {
               bsonType: "long",
               description: "must be a long and is required"
            },
            medicoNumeroDocumento: {
               bsonType: "long",
               description: "must be a long and is required"
            },
            servicioSaludId: {
               bsonType: "long",
               description: "must be a long and is required"
            },
            idOrdenDeServicio: {
               bsonType: ["long", "null"],
               description: "can be a long or null"
            }
         }
      }
   }
});

// Prestaciones Servicio collection
db.createCollection("prestaciones_servicio", {
   validator: {
      $jsonSchema: {
         bsonType: "object",
         required: ["ipsNit", "agendarCitaId", "citaRealizada"],
         properties: {
            ipsNit: {
               bsonType: "long",
               description: "must be a long and is required"
            },
            agendarCitaId: {
               bsonType: "long", 
               description: "must be a long and is required"
            },
            citaRealizada: {
               bsonType: "bool",
               description: "must be a boolean and is required"
            }
         }
      }
   }
});

// Create indexes for better performance

// Afiliados indexes
db.afiliados.createIndex({ "numeroDocumento": 1 }, { unique: true });
db.afiliados.createIndex({ "numeroDocumentoContribuyente": 1 });

// EPS indexes
db.eps.createIndex({ "nit": 1 }, { unique: true });
db.eps.createIndex({ "ipsIds": 1 });
db.eps.createIndex({ "tipoEPS": 1 });

// IPS indexes
db.ips.createIndex({ "nit": 1 }, { unique: true });
db.ips.createIndex({ "serviciosIds": 1 });

// Medicos indexes
db.medicos.createIndex({ "numeroDocumento": 1 }, { unique: true });
db.medicos.createIndex({ "ipsNit": 1 });
db.medicos.createIndex({ "especialidad": 1 });
db.medicos.createIndex({ "serviciosIds": 1 });

// Servicios de Salud indexes
db.servicios_salud.createIndex({ "idServicio": 1 }, { unique: true });
db.servicios_salud.createIndex({ "tipo": 1 });

// Ordenes de Servicio indexes
db.ordenes_servicio.createIndex({ "idOrden": 1 }, { unique: true });
db.ordenes_servicio.createIndex({ "medicoNumeroDocumento": 1 });
db.ordenes_servicio.createIndex({ "afiliadoNumeroDocumento": 1 });
db.ordenes_servicio.createIndex({ "estadoOrden": 1 });
db.ordenes_servicio.createIndex({ "serviciosIds": 1 });

// Citas indexes
db.citas.createIndex({ "idCita": 1 }, { unique: true });
db.citas.createIndex({ "afiliadoNumeroDocumento": 1 });
db.citas.createIndex({ "medicoNumeroDocumento": 1 });
db.citas.createIndex({ "servicioSaludId": 1 });
db.citas.createIndex({ "fechaHora": 1 });
db.citas.createIndex({ "idOrdenDeServicio": 1 });

// Prestaciones Servicio indexes
db.prestaciones_servicio.createIndex({ "ipsNit": 1 });
db.prestaciones_servicio.createIndex({ "agendarCitaId": 1 }, { unique: true });
db.prestaciones_servicio.createIndex({ "citaRealizada": 1 });

// Compound indexes for common queries
db.citas.createIndex({ "afiliadoNumeroDocumento": 1, "fechaHora": 1 });
db.citas.createIndex({ "medicoNumeroDocumento": 1, "fechaHora": 1 });
db.citas.createIndex({ "servicioSaludId": 1, "fechaHora": 1 });

print("MongoDB collections and indexes created successfully!");
