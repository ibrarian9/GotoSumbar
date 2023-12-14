package com.app.gotosumbar.Model;


public class TempatWisata {

	private int id;
	private String kategori, ket, nama, foto, lokasi, komem;
	private Float rate;

	public TempatWisata(){

	}

	public TempatWisata(int id, String kategori, String ket, String nama, String foto, String lokasi, String komem, Float rate) {
		this.id = id;
		this.kategori = kategori;
		this.ket = ket;
		this.nama = nama;
		this.foto = foto;
		this.lokasi = lokasi;
		this.komem = komem;
		this.rate = rate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getKomem() {
		return komem;
	}

	public void setKomem(String komem) {
		this.komem = komem;
	}

	public String getKategori() {
		return kategori;
	}

	public void setKategori(String kategori) {
		this.kategori = kategori;
	}

	public String getKet() {
		return ket;
	}

	public void setKet(String ket) {
		this.ket = ket;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public String getLokasi() {
		return lokasi;
	}

	public void setLokasi(String lokasi) {
		this.lokasi = lokasi;
	}

	public Float getRate() {
		return rate;
	}

	public void setRate(Float rate) {
		this.rate = rate;
	}
}