package com.trabalho.protocolos.sensor;

import java.io.Serializable;

/**
 * Created by charles on 08/11/15.
 */
public class Message<T> implements Serializable{

    public final static int CONTROLE = 1;
    public final static int REQUISICAO = 2;

    public int type;
    public T value;

    public void setValue( int eixo, float valor){
        this.value = (T) new Value(eixo, valor);
    }

    class Value {
        public int eixoIndex;
        public float value;

        public Value(int eixoIndex, float value) {
            this.eixoIndex = eixoIndex;
            this.value = value;
        }
    }


}
