package com.jazzkuh.mttier.data.adapters;

import com.craftmend.storm.parser.types.objects.StormTypeAdapter;
import com.jazzkuh.mttier.utils.Tier;

public class TierAdapter extends StormTypeAdapter<Tier> {
    @Override
    public Tier fromSql(Object sqlValue) {
        if (sqlValue == null) return Tier.GRAY;
        return Tier.valueOf(sqlValue.toString());
    }

    @Override
    public Object toSql(Tier value) {
        if (value == null) return Tier.GRAY.toString();
        return value.toString();
    }

    @Override
    public String getSqlBaseType() {
        return "VARCHAR(%max)";
    }

    @Override
    public boolean escapeAsString() {
        return true;
    }
}
