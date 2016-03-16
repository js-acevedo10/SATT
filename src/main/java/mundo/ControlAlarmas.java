package mundo;

import dao.AlertaDAO;
import dao.EscenarioPremodeladoDAO;
import dao.SensorDAO;
import mundo.EscenarioPremodelado.PerfilAlerta;

public class ControlAlarmas implements Runnable{

	private String idSensor;
	private String idAlerta;
	private double ultimaAltura;
	private double distancia;
	private String zona;
	
	
	
	public ControlAlarmas(String idSensor, String idAlerta, double ultimaAltura, double distancia, String zona) {
		super();
		this.idSensor = idSensor;
		this.idAlerta = idAlerta;
		this.ultimaAltura = ultimaAltura;
		this.distancia = distancia;
		this.zona = zona;
	}



	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(300000);
			SensorDTO sensor = SensorDAO.getSensor(idSensor);
			double nuevaAltura = sensor.getAltura();
			if (Math.abs(ultimaAltura-nuevaAltura)>1.5){
				String perfil = EscenarioPremodeladoDAO.getPerfilAlerta(nuevaAltura, distancia, zona);
				AlertaDAO.updateAlerta(idAlerta, perfil, nuevaAltura);
				if (!perfil.equals(PerfilAlerta.informativo.getName())){
					Thread nuevaAlerta = new Thread(new ControlAlarmas(idSensor, idAlerta, nuevaAltura, distancia, zona));
					nuevaAlerta.start();
				}
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
