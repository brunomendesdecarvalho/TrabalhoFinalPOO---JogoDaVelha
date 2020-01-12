# TrabalhoFinalPOO---JogoDaVelha

Trabalho feito para a disciplina de Programação Orientada a Objetos.

Consiste em um jogo da velha, onde o tabuleiro é representado por um vetor de strings, cada elemento representando um quadrante do tabuleiro.

O host do jogo é o jogador "X" - ou "X1" no modo "Showdown" - e o cliente é o jogador "O" - ou "X2" no modo "Showdown".

As classes do projeto estão descritas abaixo:
- A classe "Main" inicializa o jogo;
- A classe "Janela" cria a janela do jogo com as respectivas propriedades;
- A classe "Graficos" cuida de toda a parte gráfica do jogo, incluindo o que acontece a ela no ato de clicar com o mouse - checando se o clique do mouse resulta em vitória ou empate, inclusive;
- A classe "Servidor" cria os sockets e realiza as operações em cima deles;
- A classe "Som" cuida do necessário para a reprodução de efeitos de som do jogo;
- A classe "Classico" é onde serão utilizadas todas as outras classes listadas acima, e onde serão checadas a derrota ou empate do jogo, com base numa matriz que contém todas as jogadas vitoriosas possíveis;
- A classe "Showdown" é um modo de jogo próprio desse trabalho, onde ambos os jogadores são representados por "X" e, oposto ao modo clássico, o jogador que completar uma jogada, perde;

P.S. - O modo "Showdown" tem suas classes próprias quando necessário, adaptadas para ele. Ex: Graficos2, Servidor2, etc.
P.S. 2 - O modo "Showdown" apresenta o seguinte bug: quando um jogador vence, apenas ele recebe a mensagem de vitória e o efeito de som correspondente. O jogador perdedor apenas tem a sua tela congelada.
