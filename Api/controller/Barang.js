const barang =  require('../model/Barang')
const response = require('../config/response')
const mongoose = require('mongoose')
const ObjectId = mongoose.Types.ObjectId
exports.inputDataBarang = (data, gambar) =>
    new Promise(async (resolve, reject) =>{

        const barangBaru = new barang({
            kodeBarang : data.kodeBarang,
            jenisBarang : data.jenisBarang,
            namaBarang: data.namaBarang,
            stockBarang: data.stockBarang,
            hargaBarang: data.hargaBarang,
            gambar: gambar
        })
    
        await barang.findOne({kodeBarang: data.kodeBarang})
        .then(barang =>{
            if (barang){
                reject(response.commonErrorMsg('Kode Barang Sudah Digunakan'))
            }else{
                barangBaru.save()
                    .then(r =>{
                        resolve(response.commonSuccessMsg('Berhasil Menginput Data'))
                    }).catch(err =>{
                    reject(response.commonErrorMsg('Mohon Maaf Input Barang Gagal'))
                })
            }
        }).catch(err =>{
        reject(response.commonErrorMsg('Mohon Maaf Terjadi Kesalahan Pada Server kami'))
    })  
})

exports.lihatDataBarang = () =>
    new Promise(async (resolve, reject) =>{
        await barang.find({})
            .then(result =>{
                resolve(response.commonResult(result))
            })
            .catch(()=>reject(response.commonErrorMsg('Mohon Maaf Terjadi Kesalahan Pada Server kami')))
    
        })


        exports.lihatDetailDataBarang = (kodeBarang) =>
        new Promise(async (resolve, reject) =>{
            await barang.findOne({kodeBarang: kodeBarang})
                .then(result =>{
                    resolve(response.commonResult(result))
                })
                .catch(()=>reject(response.commonErrorMsg('Mohon Maaf Terjadi Kesalahan Pada Server kami')))
        })


      


    exports.updateBarang = (id, data, gambar) =>
    new Promise(async (resolve, reject)=>{
        await barang.updateOne(
            {_id : ObjectId(id)},
            {
                $set: {
                    kodeBarang : data.kodeBarang,
                    jenisBarang : data.jenisBarang,
                    namaBarang: data.namaBarang,
                    stockBarang: data.stockBarang,
                    hargaBarang: data.hargaBarang,
                    gambar: gambar
                }
            }
        ).then(barang =>{
            resolve(response.commonSuccessMsg('Berhasil Mengubah Data'))
        }).catch(err =>{
            reject(response.commonErrorMsg('Mohon Maaf Terjadi Kesalahan Pada Server kami'))
        })
    })


    exports.hapusbarang = (_id) =>
    new Promise(async (resolve, reject)=>{
        await barang.remove({_id: ObjectId(_id)})
        .then(() =>{
            resolve(response.commonSuccessMsg('Berhasil Menghapus Data'))
        }).catch(() =>{
            reject.commonErrorMs('Mohon Maaf Terjadi Kesalahan Pada Server')
        })
    })