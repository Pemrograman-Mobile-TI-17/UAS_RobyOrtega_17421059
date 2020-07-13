const mongoose = require('mongoose');

const userSchema = mongoose.Schema({

    kodeBarang: {
        type: String
    },
    jenisBarang: {
        type: String
    },
    namaBarang: {
        type: String
    },
    stockBarang: {
        type: String
    },
    hargaBarang: {
        type: String
    },
    gambar: {
        type: String
    }
})
module.exports = mongoose.model('barang', userSchema)