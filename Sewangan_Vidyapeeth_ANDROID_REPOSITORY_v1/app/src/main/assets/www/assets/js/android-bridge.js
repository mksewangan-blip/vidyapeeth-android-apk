(function(){
  window.SVAndroidNotify=function(title,message){
    try{ if(window.VidyapeethAndroid && typeof window.VidyapeethAndroid.notify==='function') window.VidyapeethAndroid.notify(String(title||'Sewangan Vidyapeeth'),String(message||'')); }catch(e){}
  };
})();
