/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [SleepNight::class], version = 1, exportSchema = false)
abstract class SleepDatabase : RoomDatabase() {
    abstract val sleepDatabaseDao: SleepDatabaseDao


    companion object {
        @Volatile   // MainMemory 에서  read, write 하기에 변수값 불일치가 일어날 일이 없다.(멀티 스레드에서), 하지만 두 개 이상의 스레드가 write 한다면 문제가 발생할 수 있기에 그럴 떈 동기화 작업을 해주는게 좋다.
        private var INSTANCE: SleepDatabase? = null

        // Multiple threads can potentially ask for a database instance at the same time,
        // resulting in two databases instead of one. This problem is not likely to happen in this sample app,
        // but it's possible for a more complex app. Wrapping the code to get the database into synchronized means
        // that only one thread of execution at a time can enter this block of code, which makes sure the database
        // only gets initialized once.
        fun getInstance(context: Context): SleepDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SleepDatabase::class.java,
                        "sleep_history_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
