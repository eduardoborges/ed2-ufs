class Aluno {
	private long 	matric; 	// 8 bytes
	private String 	nome; 		// 60 bytes
	private String 	endereco; 	// 80 bites
	private String 	email; 		// 50 bytes
	private short 	curso; 		// 2 bytes

	/**
	0      8        68         148      198     200
	===============================================
	|matric|  nome  | endereco  |  email | curso  |
	===============================================

	*/

	public Aluno(){ }

	public Aluno(long matric, String nome, String endereco, String email, short curso){
		this.matric 	= matric;
		this.nome 		= nome;
		this.endereco 	= endereco;
		this.email 		= email;
		this.curso 		= curso;
	}

	// gets
	public long getMatric(){
		return this.matric;
	}

	public String getNome(){
		return this.nome;
	}

	public String getEndereco(){
		return this.endereco;
	}

	public String getEmail(){
		return this.email;
	}
	
	public short getCurso(){
		return this.curso;
	}

	// sets
	public void setMatric(long matric){
		this.matric = matric;
	}

	public void setNome(String nome){
		this.nome = nome;
	}

	public void setEndereco(String endereco){
		this.endereco = endereco;
	}

	public void setEmail(String email){
		this.email = email;
	}
	
	public void setCurso(short curso){
		this.curso = curso;
	}

}