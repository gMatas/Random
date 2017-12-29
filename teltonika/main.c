#include <stdio.h>
#include <stdlib.h>
#include <string.h>

//////////////////////////////////////////////////////////////
const char failPav_1[] = "./new/events.lua";            //Pirmas failas
const char failPav_2[] = "./new/index.htm";             //Antras failas
const char failPav_3[] = "./new/netinfo_wimax.htm";     //Trecias failas
const char failPav_4[] = "./new/profiles.htm";          //Ketvirtas failas
const char failPav_5[] = "./new/profiles.htm";          //Penktas failas
const char failPav_R[] = "results.txt";                 //Rezultatu failas
//////////////////////////////////////////////////////////////
void FailoNuskaitymas(FILE *rodykle, char pavadinimasF[]);



int main(){

    FILE *failoRod_1;                   //Failo rodykle

    FILE *rez;                          //Failo rezultatu rodykle
        rez = fopen(failPav_R,"w");     //Sukuriamas rez failas
        fclose(rez);

    FailoNuskaitymas(failoRod_1, failPav_1);
    FailoNuskaitymas(failoRod_1, failPav_2);
    FailoNuskaitymas(failoRod_1, failPav_3);
    FailoNuskaitymas(failoRod_1, failPav_4);
    FailoNuskaitymas(failoRod_1, failPav_5);

    return 0;
}

void FailoNuskaitymas(FILE *rodykle,char pavadinimasF[]){

    char *pagRodykle;                      //Pagalbine rodykle
    rodykle = fopen(pavadinimasF,"r");     //Atidaromas failas su read teisem

    if(!rodykle){

        printf("Nera failo!\n");
        fclose(rodykle);                   //Uzdaromas failas
    }

    else {

        int skaitliukas = 0;

        char eilute[150];

        FILE *rez;                         //Failo rodykle
        rez = fopen(failPav_R,"a");        //Papildomas rez failas rezultatais
        fprintf(rez, pavadinimasF);


        fprintf(rez,"\n");

        char ftagas_1[] = "translate";
        char ftagas_2[] = "translatef";

        while(!feof(rodykle)){             //Iki failo pabaigos

               fgets(eilute,150,rodykle);  //Nuskaitoma eilute
               skaitliukas++;

               pagRodykle = strstr(eilute, ftagas_1);

               /*if(pagRodykle){

                printf("%d",skaitliukas);

               }*/
               char *vardas = "labas";
                //fprintf(rez,vardas);
                char name [100];
                strcpy(name, vardas);
                fprintf(rez, name);


                //fprintf(rez, name);

               //fprintf(rez, pagRodykle);


    }

    fprintf(rez,"\n");
    fclose(rodykle);
    fclose(rez);

    }
}
