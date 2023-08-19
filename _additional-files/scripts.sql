
//--Create table to input events
create Table event_tab(id TEXT primary key, name TEXT, description TEXT, location TEXT,event_date TEXT, start_time TEXT, end_time TEXT)


//--Drop table
drop Table if exists event_tab


//--Insert event values (Java function)
    public Boolean insertuserdata(String name, String location, String description, String event_date, String start_time, String end_time)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String id = name+"^"+event_date+"^"+start_time+"^"+end_time;
        contentValues.put("id", id);
        contentValues.put("event_date", event_date);
        contentValues.put("start_time", start_time);
        contentValues.put("end_time", end_time);
        contentValues.put("name", name);
        contentValues.put("description", description);
        contentValues.put("location", location);
        long result=DB.insert("event_tab", null, contentValues);

        //--return whether the entry was a success or not
        if(result==-1){
            return false;
        }else{
            return true;
        }
    }