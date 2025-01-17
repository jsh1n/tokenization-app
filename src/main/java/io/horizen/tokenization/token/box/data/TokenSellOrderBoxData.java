package io.horizen.tokenization.token.box.data;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.primitives.Bytes;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import com.horizen.box.data.AbstractNoncedBoxData;
import com.horizen.box.data.NoncedBoxDataSerializer;
import io.horizen.tokenization.token.box.TokenSellOrderBox;
import io.horizen.tokenization.token.proposition.SellOrderProposition;
import io.horizen.tokenization.token.proposition.SellOrderPropositionSerializer;
import com.horizen.serialization.Views;
import scorex.crypto.hash.Blake2b256;

import java.util.Arrays;

@JsonView(Views.Default.class)
public final class TokenSellOrderBoxData extends AbstractNoncedBoxData<SellOrderProposition, TokenSellOrderBox, TokenSellOrderBoxData> {

    // Car sell order attributes is similar to car attributes.
    // The only change is that Sell order contains the car price as well.
    private final String id;
    private final String type;

    public TokenSellOrderBoxData(SellOrderProposition proposition, long price, String id, String type) {
        super(proposition, price);
        this.id = id;
        this.type = type;
    }

    public String getID() {
        return id;
    }

    public String getType() {
        return type;
    }


    @Override
    public TokenSellOrderBox getBox(long nonce) {
        return new TokenSellOrderBox(this, nonce);
    }

    @Override
    public byte[] customFieldsHash() {
        return Blake2b256.hash(
                Bytes.concat(
                        id.getBytes(),
                        type.getBytes()));
    }

    @Override
    public NoncedBoxDataSerializer serializer() {
        return TokenSellOrderBoxDataSerializer.getSerializer();
    }

    @Override
    public byte boxDataTypeId() {
        return TokenBoxesDataIdsEnum.TokenSellOrderBoxDataId.id();
    }

    @Override
    public byte[] bytes() {
        return Bytes.concat(
                Ints.toByteArray(proposition().bytes().length),
                proposition().bytes(),
                Longs.toByteArray(value()),
                Ints.toByteArray(id.getBytes().length),
                id.getBytes(),
                Ints.toByteArray(type.getBytes().length),
                type.getBytes()
        );
    }

    public static TokenSellOrderBoxData parseBytes(byte[] bytes) {
        int offset = 0;

        int size = Ints.fromByteArray(Arrays.copyOfRange(bytes, offset, offset + Ints.BYTES));
        offset += Ints.BYTES;

        SellOrderProposition proposition = SellOrderPropositionSerializer.getSerializer()
                .parseBytes(Arrays.copyOfRange(bytes, offset, offset + size));
        offset += size;

        long price = Longs.fromByteArray(Arrays.copyOfRange(bytes, offset, offset + Longs.BYTES));
        offset += Longs.BYTES;

        size = Ints.fromByteArray(Arrays.copyOfRange(bytes, offset, offset + Ints.BYTES));
        offset += Ints.BYTES;

        String id = new String(Arrays.copyOfRange(bytes, offset, offset + size));
        offset += size;

        size = Ints.fromByteArray(Arrays.copyOfRange(bytes, offset, offset + Ints.BYTES));
        offset += Ints.BYTES;

        String type = new String(Arrays.copyOfRange(bytes, offset, offset + size));

        return new TokenSellOrderBoxData(proposition, price, id, type);
    }

    @Override
    public String toString() {
        return "TokenSellOrderBoxData{" +
                "id=" + id +
                ", proposition=" + proposition() +
                ", value=" + value() +
                ", type=" + type +
                '}';
    }
}
