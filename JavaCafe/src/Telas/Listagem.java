package Telas;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import Interfaces.InventarioIU;

public class Listagem implements InventarioIU, Serializable {
    private static final long serialVersionUID = 1L;
    private Map<String, Produto> produtos;

    // Construtor que inicializa o mapa de produtos
    public Listagem() {
        produtos = new HashMap<>();
    }

    // Adiciona um produto ao mapa
    @Override
    public void addProduto(Produto produto) {
        produtos.put(produto.nomeGet(), produto);
    }

    // Pesquisa um produto pelo nome
    public Produto pesquisarProduto(String nome) {
        return produtos.get(nome);
    }

    // Deleta um produto do mapa pelo nome
    public void deletarProduto(String nomeProduto) {
        produtos.remove(nomeProduto);
    }

    // Carrega o inventário de um arquivo e retorna uma instância de Listagem
    public static Listagem carregarInventario(String caminhoArquivo) throws IOException {
        Listagem listagem = new Listagem();
        try (BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(",");
                String nome = partes[0];
                double preco = Double.parseDouble(partes[1]);
                int quantidade = Integer.parseInt(partes[2]);
                listagem.addProduto(new Produto(nome, preco, quantidade));
            }
        }
        return listagem;
    }

    // Salva o inventário em um arquivo
    public void salvarInv(String caminhoArquivo) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivo))) {
            for (Produto produto : produtos.values()) {
                writer.write(produto.nomeGet() + "," + produto.valor() + "," + produto.quantidadePegar());
                writer.newLine();
            }
        }
    }

    // Retorna o mapa de produtos
    public Map<String, Produto> getProdutos() {
        return produtos;
    }
}
