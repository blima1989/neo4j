package br.carona.neo4j;


import java.util.Scanner;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class Amizade {
	private static final String DB_PATH = "C:\\bin\\projetos\\prjNeo4j\\db";

	String myString;
	GraphDatabaseService graphDB;
	Node meuPrimeiroNo;
	Node meuSegundoNo;
	Relationship myRelationship;

	private static enum RelTypes implements RelationshipType {
		KNOWS

	}

	public static void main(String[] args) {

		
		Amizade myNeoInstance = new Amizade();
		
		
		Scanner input = new Scanner(System.in);
		
		System.out.println("Escolha uma das opções abaixo");
		System.out.println("[1] - Criar nós \n [2] - Remover nós \n [3] - Desligar o banco e encerrar a aplicação: ");
		String resp = input.next();
		
		switch (resp){
		case "1": myNeoInstance.criarDB(); 
		break;
		case "2": myNeoInstance.removerDados();
		break;
		case "3": myNeoInstance.desligarBanco();
		break;
		default: System.out.println("Opção Invalida!! O programa será encerrado.");
		}
		

	}

	@SuppressWarnings("deprecation")
	void criarDB(){
		graphDB = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
		Scanner input = new Scanner(System.in);
		Transaction tx = graphDB.beginTx();
		try{
			meuPrimeiroNo = graphDB.createNode();
			
			System.out.println("Digite o seu nome: ");
			String nome1 = input.next();
			meuPrimeiroNo.setProperty("nome", nome1);
				
			meuSegundoNo = graphDB.createNode();
			System.out.println("Digite um outro nome: ");
			String nome2 = input.next();			
			meuSegundoNo.setProperty("nome", nome2);
						
			myRelationship = meuPrimeiroNo.createRelationshipTo(meuSegundoNo, RelTypes.KNOWS);
			myRelationship.setProperty("relationship-type", "conhece");
			
			myString = (meuPrimeiroNo.getProperty("nome".toString()))
					+ " " + (myRelationship.getProperty("relationship-type").toString())
					+ " " + (meuSegundoNo.getProperty("nome").toString());
			
			System.out.println(myString);
			tx.success();
		}
		finally{
			tx.finish();
		}
		
	}

	void removerDados() {
		Transaction tx = graphDB.beginTx();
		
		try{
			meuPrimeiroNo.getSingleRelationship(RelTypes.KNOWS, Direction.OUTGOING).delete();
			System.out.println("Removendo os nós...");
			meuPrimeiroNo.delete();
			meuSegundoNo.delete();
			tx.success();
		}
		finally{
			tx.finish();
		}

	}

	void desligarBanco() {
		graphDB.shutdown();
		System.out.println("O banco de dados foi desligado.");

	}
}
