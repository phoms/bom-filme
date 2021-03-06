package br.com.infnet.bomfilme.managedbean;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import br.com.infnet.bomfilme.dto.MidiaAlugadaDTO;
import br.com.infnet.bomfilme.filtro.FiltroFilme;
import br.com.infnet.bomfilme.filtro.FiltroMidiasAlugadas;
import br.com.infnet.bomfilme.model.CarrinhoItem;
import br.com.infnet.bomfilme.model.Exemplar;
import br.com.infnet.bomfilme.model.Filme;
import br.com.infnet.bomfilme.model.Profissional;
import br.com.infnet.bomfilme.model.Reserva;
import br.com.infnet.bomfilme.model.constant.StatusExemplar;
import br.com.infnet.bomfilme.service.FilmeService;

@ManagedBean(name = "filmeBean")
@ViewScoped
public class FilmeBean {
	@ManagedProperty(value = "#{carrinhoBean}")
	private CarrinhoBean carrinhoBean;
	@ManagedProperty(value = "#{userSessionBean}")
	private UserSessionBean userSessionBean;
	
	@Inject
	private FiltroFilme filtro;
	@Inject
	private FiltroMidiasAlugadas filtroMidiasAlugadas;
	@Inject
	private FilmeService filmeService;
	@Inject
	private Reserva reserva;
	
	private List<Filme> filmes;
	
	private List<MidiaAlugadaDTO> midiasAlugadas;

	private Date dataReserva;
	
	public void pesquisar() {
		filmes = filmeService.pesquisarFilmes(filtro);
	}

	public Set<Profissional> lerAtores() {
		return filmeService.listarAtores();
	}

	public long lerQuantidadeExemplares(Filme filme, String tipoMidia) {
		long qtd = filme.getExemplares()
								.stream()
								.filter(e -> e.getTipoMidia().equals(tipoMidia) && 
												e.getStatus().equals(StatusExemplar.DISPONIVEL))
								.count();
		return qtd;
	}

	public void incluirItemCarrinho(Filme filme, String tipoMidia) {
		if(userSessionBean.getUsuarioLogado() != null) {
			filmeService.verificarHistorico(filme, userSessionBean.getUsuarioLogado());
		}
		
		CarrinhoItem item = new CarrinhoItem(filme, tipoMidia);
		carrinhoBean.getItens().add(item);
		
		FacesContext.getCurrentInstance().addMessage("lista-filmes", new FacesMessage(FacesMessage.SEVERITY_INFO, "Filme inclu�do no carrinho!", null));
	}

	/**
	 * Inicia o processo de reserva de um filme.
	 * 
	 * @param filme
	 * @param tipoMidia
	 */
	public void iniciarReserva(Filme filme, String tipoMidia) {
		Exemplar exemplar = filme.getExemplares()
									.stream()
									.filter(e -> e.getTipoMidia().equals(tipoMidia) &&
													e.getStatus().equals(StatusExemplar.DISPONIVEL))
									.findFirst()
									.orElse(null);
		
		reserva.setFilme(filme);
		reserva.setExemplar(exemplar);
	}
	
	/**
	 * Realiza a reserva de um filme.
	 */
	public void reservar() {
		Instant instant = Instant.ofEpochMilli(this.getDataReserva().getTime());
		LocalDate dataReserva = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
		
		reserva.setDataReserva(dataReserva);
		filmeService.reservarFilme(reserva);
		
		userSessionBean.getUsuarioLogado().getReservas().add(reserva);
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, 
									"Reserva efetuada!.", null));
	}
	
	/**
	 * Lista todas as m�dias alugadas da locadora.
	 */
	public void pesquisarMidiasAlugadas() {
		midiasAlugadas = filmeService.pesquisarMidiasAlugadas(filtroMidiasAlugadas);
	}
	
	public List<Filme> getFilmes() {
		return filmes;
	}

	public FiltroFilme getFiltro() {
		return filtro;
	}

	public void setFiltro(FiltroFilme filtro) {
		this.filtro = filtro;
	}

	public CarrinhoBean getCarrinhoBean() {
		return carrinhoBean;
	}

	public void setCarrinhoBean(CarrinhoBean carrinhoBean) {
		this.carrinhoBean = carrinhoBean;
	}

	public Reserva getReserva() {
		return reserva;
	}

	public void setReserva(Reserva reserva) {
		this.reserva = reserva;
	}

	public UserSessionBean getUserSessionBean() {
		return userSessionBean;
	}

	public void setUserSessionBean(UserSessionBean userSessionBean) {
		this.userSessionBean = userSessionBean;
	}

	public Date getDataReserva() {
		return dataReserva;
	}

	public void setDataReserva(Date dataReserva) {
		this.dataReserva = dataReserva;
	}

	public FiltroMidiasAlugadas getFiltroMidiasAlugadas() {
		return filtroMidiasAlugadas;
	}

	public void setFiltroMidiasAlugadas(FiltroMidiasAlugadas filtroMidiasAlugadas) {
		this.filtroMidiasAlugadas = filtroMidiasAlugadas;
	}

	public List<MidiaAlugadaDTO> getMidiasAlugadas() {
		return midiasAlugadas;
	}

	public void setMidiasAlugadas(List<MidiaAlugadaDTO> midiasAlugadas) {
		this.midiasAlugadas = midiasAlugadas;
	}
}