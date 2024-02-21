package ule.edi.travel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ule.edi.model.*;

public class TravelArrayImpl implements Travel {

	private static final Double DEFAULT_PRICE = 100.0;
	private static final Byte DEFAULT_DISCOUNT = 25;
	private static final Byte CHILDREN_EXMAX_AGE = 18;
	private Date travelDate;
	private int nSeats;

	private Double price; // precio de entradas
	private Byte discountAdvanceSale; // descuento en venta anticipada (0..100)

	private Seat[] seats;

	public TravelArrayImpl(Date date, int nSeats) {

		// utiliza los precios por defecto: DEFAULT_PRICE y DEFAULT_DISCOUNT definidos
		// en esta clase
		// debe crear el array de asientos
		this.travelDate = date;
		this.nSeats = nSeats;

		this.discountAdvanceSale = DEFAULT_DISCOUNT;
		this.price = DEFAULT_PRICE;

		this.seats = new Seat[nSeats];

	}

	public TravelArrayImpl(Date date, int nSeats, Double price, Byte discount) {

		// Debe crear el array de asientos
		this.travelDate = date;
		this.nSeats = nSeats;

		this.discountAdvanceSale = discount;
		this.price = price;

		this.seats = new Seat[nSeats];
	}

	@Override
	public Byte getDiscountAdvanceSale() {
		return this.discountAdvanceSale;
	}

	@Override
	public int getNumberOfSoldSeats() {
		int sold = 0;
		for (Seat s : this.seats) {
			if (s != null) {
				sold++;
			}
		}
		return sold;
	}

	@Override
	public int getNumberOfNormalSaleSeats() {
		int n = 0;
		for (Seat s : this.seats) {
			if (s != null && !s.getAdvanceSale()) {
				n++;
			}
		}
		return n;
	}

	@Override
	public int getNumberOfAdvanceSaleSeats() {
		int n = 0;
		for (Seat s : this.seats) {
			if (s != null && s.getAdvanceSale()) {
				n++;
			}
		}
		return n;

	}

	@Override
	public int getNumberOfSeats() {
		return nSeats;
	}

	@Override
	public int getNumberOfAvailableSeats() {
		return nSeats - this.getNumberOfSoldSeats();
	}

	@Override
	public Seat getSeat(int pos) {
		return this.seats[pos - 1];
	}

	@Override
	public Person refundSeat(int pos) {

		// Seat asiento = this.seats[pos - 1];
		// this.seats[pos - 1] = null;
		// if (asiento != null) {
		// 	return asiento.getHolder();
		// }
		if (pos>0 && pos<nSeats+1){
			Seat s=null;
			s=this.getSeat(pos);
			this.seats[pos-1]=null;

			return s.getHolder();
		}
		return null;

	}

	private boolean isChildren(int age) {
		return age < CHILDREN_EXMAX_AGE;
	}

	private boolean isAdult(int age) {

		return age >= CHILDREN_EXMAX_AGE;
	}

	@Override
	public List<Integer> getAvailableSeatsList() {
		List<Integer> lista = new ArrayList<Integer>(nSeats);

		for (int i = 0; i < nSeats; i++) {
			if (this.seats[i] == null) {
				lista.add(i + 1);
			}
		}

		return lista;
	}

	@Override
	public List<Integer> getAdvanceSaleSeatsList() {
		List<Integer> lista = new ArrayList<Integer>(nSeats);

		for (int i = 0; i < nSeats; i++) {
			if (this.seats[i] != null && this.seats[i].getAdvanceSale()) {
				lista.add(i + 1);
			}
		}

		return lista;
	}

	@Override
	public int getMaxNumberConsecutiveSeats() {
		int max = 0;
		int n = 0;

		for (Seat s : this.seats) {
			if (s == null) {
				n++;
			} else {
				max = Integer.max(max, n);
			}
		}
		return max;
	}

	@Override
	public boolean isAdvanceSale(Person p) {

		for (Seat s : this.seats) {
			if (s!=null && p.equals(s.getHolder()) && s.getAdvanceSale()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Date getTravelDate() {
		return this.travelDate;
	}

	@Override
	public boolean sellSeatPos(int pos, String nif, String name, int edad, boolean isAdvanceSale) {

		if (pos > 0 && pos <= this.nSeats && this.seats[pos - 1] == null) {

			this.seats[pos - 1] = new Seat(isAdvanceSale, new Person(nif, name, edad));
			return true;
		}
		return false;
	}

	@Override
	public int getNumberOfChildren() {
		int number = 0;

		for (Seat s : this.seats) {
			if (s != null && this.isChildren(s.getHolder().getAge())) {
				number++;
			}

		}

		return number;
	}

	@Override
	public int getNumberOfAdults() {

		int number = 0;

		for (Seat s : this.seats) {
			if (s != null && this.isAdult(s.getHolder().getAge())) {
				number++;
			}

		}

		return number;

	}

	@Override
	public Double getCollectionTravel() {

		return this.price * this.getNumberOfNormalSaleSeats()
				+ this.getNumberOfAdvanceSaleSeats() * (this.price - (this.price * this.discountAdvanceSale / 100));
	}

	@Override
	public int getPosPerson(String nif) {
		for (int i = 0; i < this.nSeats; i++) {
			if (this.seats[i] != null && this.seats[i].getHolder().getNif() == nif) {
				return i + 1;
			}
		}
		return -1;
	}

	@Override
	public int sellSeatFrontPos(String nif, String name, int edad, boolean isAdvanceSale) {
		int pos = -1;

		for (int i = 0; i < nSeats; i++) {
			if (this.seats[i] == null) {
				pos = i + 1;
				this.seats[i] = new Seat(isAdvanceSale, new Person(nif, name, edad));
				break;
			}
		}

		return pos;
	}

	@Override
	public int sellSeatRearPos(String nif, String name, int edad, boolean isAdvanceSale) {
		int pos = -1;

		for (int i = nSeats - 1; i <= 0; i--) {
			if (this.seats[i] == null) {
				pos = i + 1;
				this.seats[i] = new Seat(isAdvanceSale, new Person(nif, name, edad));
				break;
			}
		}

		return pos;

	}

	@Override
	public Double getSeatPrice(Seat seat) {
		Double price = this.price;
		if (seat.getAdvanceSale()) {
			price -= this.price * this.discountAdvanceSale / 100;

		}
		return price;
	}

	@Override
	public double getPrice() {
		return this.price;
	}
}