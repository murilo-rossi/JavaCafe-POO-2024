/**
 * Classe que representa um pedido de produtos.
 */
package Telas;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import javax.swing.*;
import java.io.*;
import java.text.SimpleDateFormat;

public class Ordem implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Produto> itens;
    private double total;

    /**
     * Construtor padrão que inicializa a lista de itens e o total do pedido.
     */
    public Ordem() {
        this.itens = new ArrayList<>();
        this.total = 0.0;
    }

    /**
     * Adiciona um item ao pedido.
     * Se a quantidade desejada for maior que a quantidade disponível em estoque, exibe uma mensagem de erro.
     * @param produto o produto a ser adicionado
     * @param quantidade a quantidade do produto a ser adicionada
     */
    public void addItem(Produto produto, int quantidade) {
        if (produto.getQuantidade() >= quantidade) {
            produto.setQuantidade(produto.getQuantidade() - quantidade);
            for (int i = 0; i < quantidade; i++) {
                itens.add(produto);
            }
            total += produto.getPreco() * quantidade;
        } else {
            JOptionPane.showMessageDialog(null, "Nao sera possivel, temos apenas " + produto.getQuantidade() + " unidades \n no estoque para o produto " + produto.getNome(), "Estoque Insuficiente", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Remove um item do pedido.
     * Se a quantidade desejada para remoção exceder a quantidade no pedido, exibe uma mensagem de erro.
     * @param nomeProduto o nome do produto a ser removido
     * @param quantidade a quantidade do produto a ser removida
     */
    public void removerItem(String nomeProduto, int quantidade) {
        int count = 0;
        for (int i = itens.size() - 1; i >= 0; i--) {
            Produto p = itens.get(i);
            if (p.getNome().equals(nomeProduto) && count < quantidade) {
                itens.remove(i);
                p.setQuantidade(p.getQuantidade() + 1);
                total -= p.getPreco();
                count++;
            }
        }
        if (count < quantidade) {
            JOptionPane.showMessageDialog(null, "A quantidade para remoção excede a quantidade no pedido", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Retorna a lista de itens do pedido.
     * @return a lista de itens do pedido
     */
    public List<Produto> getItens() {
        return itens;
    }

    /**
     * Calcula e retorna o total do pedido.
     * @return o total do pedido
     */
    public double calTotal() {
        return total;
    }

    /**
     * Finaliza o pedido e exibe o total no console.
     */
    public void finalizarPedido() {
        System.out.println("Pedido finalizado. Total: R$ " + calTotal());
    }

    /**
     * Salva os detalhes do pedido em um arquivo.
     * @param caminhoArquivo o caminho do arquivo onde os detalhes do pedido serão salvos
     * @throws IOException se houver um erro ao escrever no arquivo
     */
    public void salvarPedido(String caminhoArquivo) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivo, true))) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            writer.write("Data do Pedido: " + sdf.format(new Date()));
            writer.newLine();
            writer.newLine();

            // Agrupa itens por nome para evitar duplicatas
            Map<String, Integer> quantidadeMap = new HashMap<>();
            Map<String, Double> precoMap = new HashMap<>();

            for (Produto p : itens) {
                quantidadeMap.put(p.getNome(), quantidadeMap.getOrDefault(p.getNome(), 0) + 1);
                precoMap.put(p.getNome(), p.getPreco());
            }

            for (String nome : quantidadeMap.keySet()) {
                writer.write(String.format("Produto: " + nome + " | Preço Unitário: " + precoMap.get(nome) + " | Quantidade: " + quantidadeMap.get(nome)));
                writer.newLine();
            }

            writer.newLine();
            writer.write("VALOR             R$ " + String.format("%.2f", total));
            writer.newLine();
            writer.newLine();
            writer.write("-------------------------------------------------------");
            writer.newLine();
            writer.newLine();
        }
    }

    /**
     * Retorna a representação em string do pedido.
     * @return a representação em string do pedido
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Pedido:\n");
        for (Produto p : itens) {
            sb.append(p.getNome()).append(" - R$ ").append(p.getPreco()).append("\n");
        }
        return sb.toString();
    }
}
