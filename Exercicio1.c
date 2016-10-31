/* Programa criado para converter uma ER (expressao regular) em um e-AFN (automato finito nao deterministico)
   Faz parte do Trabalho 1, exercicio 1 da matéria CT-200.
*/
#include <stdio.h>
#include <stdlib.h>
// definicao da estrutura para o grafo do e-AFN
typedef struct {
    int EstIni;
    int EstFim;
    char Arco[8001];
} Grafo;

void Converter_ER(Grafo *eAFN, int n, int nextno){

    int num_par = 0;
    int c = 0;
    int cn = 0;
    char flag_fim = 'N';

    int cont = 0;

    // primeiro loop
    while ((strlen(eAFN[n].Arco) == 1 )) {
        n = n + 1;
    }

    if ((strlen(eAFN[n].Arco) == 0)){
        return;
    } else {
        while (flag_fim != 'S' || cont != 3){

            switch (eAFN[n].Arco[c]){
                case '(' :
                    num_par = 1;
                    c = c + 1;
                    do {
                        switch (eAFN[n].Arco[c]) {
                            case '(' :
                                c = c + 1;
                                num_par = num_par + 1;
                                break;
                            case ')' :
                                c = c + 1;
                                num_par = num_par - 1;
                                break;
                            default :
                                c = c + 1;
                                break;
                        }
                    } while ((num_par != 0) && (eAFN[n].Arco[c] != '\0'));

                    break;

                case '+' :

                    eAFN[n+4].EstIni = eAFN[n+3].EstIni;
                    eAFN[n+4].EstFim = eAFN[n+3].EstFim;
                    strcpy(eAFN[n+4].Arco,eAFN[n+3].Arco);

                    eAFN[n+3].EstIni = eAFN[n+2].EstIni;
                    eAFN[n+3].EstFim = eAFN[n+2].EstFim;
                    strcpy(eAFN[n+3].Arco,eAFN[n+2].Arco);

                    eAFN[n+2].EstIni = eAFN[n+1].EstIni;
                    eAFN[n+2].EstFim = eAFN[n+1].EstFim;
                    strcpy(eAFN[n+2].Arco,eAFN[n+1].Arco);

                    eAFN[n+1].EstIni = eAFN[n].EstIni;
                    eAFN[n+1].EstFim = eAFN[n].EstFim;
                    eAFN[n].Arco[c] = '\0' ;
                    c = c + 1;
                    while (eAFN[n].Arco[c] != '\0'){
                        eAFN[n+1].Arco[cn] = eAFN[n].Arco[c];
                        c = c + 1 ;
                        cn = cn + 1; }
                        eAFN[n+1].Arco[cn] = '\0' ;
                    flag_fim = 'S';
                    break;
                default  :
                    if (eAFN[n].Arco[c] == '\0'){
                        flag_fim = 'S';
                    } else {
                        c = c + 1;
                    }
                    break;
            }
        }
        c = 0;
        flag_fim != 'N';
        if (eAFN[n].Arco[c] == '('){
            num_par = 1;
            c = c + 1;
            do {
                switch (eAFN[n].Arco[c]) {
                    case '(' :
                        c = c + 1;
                        num_par = num_par + 1;
                        break;
                    case ')' :
                        c = c + 1;
                        num_par = num_par - 1;
                        break;
                    default :
                        c = c + 1;
                        break;
                }
            } while ((num_par != 0) && (eAFN[n].Arco[c] != '\0'));

            if (eAFN[n].Arco[c] == '*'){
                c = c + 1;
            }
        } else {
            if (eAFN[n].Arco[c] != '\0') {
                c = c + 1;
                if (eAFN[n].Arco[c] == '*'){
                    c = c + 1;
                }
            }
        }

        if (eAFN[n].Arco[c] != '\0'){
            eAFN[n+4].EstIni = eAFN[n+3].EstIni;
            eAFN[n+4].EstFim = eAFN[n+3].EstFim;
            strcpy(eAFN[n+4].Arco,eAFN[n+3].Arco);

            eAFN[n+3].EstIni = eAFN[n+2].EstIni;
            eAFN[n+3].EstFim = eAFN[n+2].EstFim;
            strcpy(eAFN[n+3].Arco,eAFN[n+2].Arco);

            eAFN[n+2].EstIni = eAFN[n+1].EstIni;
            eAFN[n+2].EstFim = eAFN[n+1].EstFim;
            strcpy(eAFN[n+2].Arco,eAFN[n+1].Arco);

            nextno = nextno + 1;

            eAFN[n].EstIni   = eAFN[n].EstIni;
            eAFN[n+1].EstIni = nextno;

            eAFN[n+1].EstFim = eAFN[n].EstFim;
            eAFN[n].EstFim   = eAFN[n+1].EstIni;

            eAFN[n+1].Arco[0] = eAFN[n].Arco[c];
            eAFN[n].Arco[c]   = '\0';
            c = c + 1;
            cn = 1;
            while (eAFN[n].Arco[c] != '\0'){
               eAFN[n+1].Arco[cn] = eAFN[n].Arco[c];
               c = c + 1 ;
               cn = cn + 1;
            }
            eAFN[n+1].Arco[cn] = '\0' ;
        }

        c = 0;
        if (eAFN[n].Arco[c] == '('){
            num_par = 1;
            c = c + 1;
            do {
                switch (eAFN[n].Arco[c]) {
                case '(' :
                    c = c + 1;
                    num_par = num_par + 1;
                    break;
                case ')' :
                    c = c + 1;
                    num_par = num_par - 1;
                    break;
                default :
                   c = c + 1;
                   break;
                }
            } while ((num_par != 0) && (eAFN[n].Arco[c] != '\0'));
        } else {
            c = c + 1 ;
        }

        if (eAFN[n].Arco[c] != '\0') {
            if (eAFN[n].Arco[c] == '*'){
                eAFN[n].Arco[c] = '\0';

                eAFN[n+3].EstIni = eAFN[n+1].EstIni;
                eAFN[n+3].EstFim = eAFN[n+1].EstFim;
                strcpy(eAFN[n+3].Arco,eAFN[n+1].Arco);

                eAFN[n+4].EstIni = eAFN[n+2].EstIni;
                eAFN[n+4].EstFim = eAFN[n+2].EstFim;
                strcpy(eAFN[n+4].Arco,eAFN[n+2].Arco);

                nextno = nextno + 1;

                eAFN[n].EstIni   = eAFN[n].EstIni;
                eAFN[n+1].EstIni = nextno;

                eAFN[n+1].EstFim = eAFN[n].EstFim;
                eAFN[n].EstFim   = eAFN[n+1].EstIni;

                eAFN[n+2].EstIni = eAFN[n].EstFim;
                eAFN[n+2].EstFim = eAFN[n+1].EstIni;

                strcpy(eAFN[n+2].Arco,eAFN[n].Arco) ;
                eAFN[n].Arco[0]   = '&';
                eAFN[n].Arco[1]   = '\0';
                eAFN[n+1].Arco[0] = '&';
                eAFN[n+1].Arco[1] = '\0';
            }
        }

        c = 0;
        if (eAFN[n].Arco[c] == '('){
            while (eAFN[n].Arco[c+1] != '\0'){
                eAFN[n].Arco[c] = eAFN[n].Arco[c+1];
                c = c + 1;
            }
            eAFN[n].Arco[c-1] = '\0' ;
        }
        Converter_ER(eAFN, n, nextno);
    }
}

int main(void){
//****************************************************************************************
// dada a caracteristica finita de um automato esse algoritmo limita a 8000 símbolos a ER
//****************************************************************************************
    char ExpReg[8001];
    char pegachar;
    int i = 0;
    printf("\nDigite a Expressao Regular que deve ser convertida em um e-AFN\nER:");
    do {
        pegachar = getchar();
        ExpReg[i] = pegachar;
        i = i + 1;
    } while (pegachar != '\n');
    ExpReg[i-1] = '\0';

//****************************************************************************************

    Grafo *eAFN = (Grafo*) malloc(8001 * sizeof(Grafo));

    if (eAFN == NULL){
        printf ("/n************************************************");
        printf ("ALOCACAO DE MEMORIA DINAMICA NAO ACONTECEU - ERRO ");
        printf ("/n************************************************");
        exit(1);
    }
    int n = 0;
    int nextno = 1;
    eAFN[0].EstIni = 0; // inicia primeiro objeto com estado 0 e final 1
    eAFN[0].EstFim = 1;
    strcpy(eAFN[0].Arco, ExpReg); // joga regex pra Arco do objeto 1

    // array, 0, 1
    Converter_ER(eAFN, n, nextno);

    FILE *arquivo;
    arquivo = fopen("Exercicio1-CT200.txt", "w");
    fprintf(arquivo, "digraph  E_AFN                  {  \n");
    fprintf(arquivo, "\trankdir=LR;                      \n");
    fprintf(arquivo, "\tsize=\"8,5\"                     \n");
    fprintf(arquivo, "\tnode [shape = doublecircle]; 1;  \n");
    fprintf(arquivo, "\tnode [shape = circle]            \n");

    // enquanto houver nós
    n = 0;
    while (strlen(eAFN[n].Arco) != 0) {
        fprintf(arquivo, "\t%i -> %i [ label = \"%s\" ];   \n", eAFN[n].EstIni, eAFN[n].EstFim, eAFN[n].Arco);
        n = n + 1;
    }

    fprintf(arquivo, "}");

    printf("\nExpressao:%s:Convertida com sucesso.Ver arq. Exercicio1-CT200.txt", ExpReg);

    system("pause");
    return 0;
}
