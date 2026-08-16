package com.fouad.winboard
import android.content.Context
import android.media.SoundPool
object KeyboardSoundManager{ var instance: KeyboardSoundManager?=null; private lateinit var pool: SoundPool; fun init(c: Context){ instance=this; pool=SoundPool.Builder().setMaxStreams(6).build() }; fun play(){ try{ pool.play(1,0.8f,0.8f,1,0,1f) }catch(e:Exception){} } }
