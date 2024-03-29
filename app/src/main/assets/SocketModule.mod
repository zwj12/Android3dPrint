MODULE SocketModule
    VAR socketdev socketServer;
    VAR socketdev socketClient;
    VAR string strIPAddressServer:="127.0.0.1";
    VAR num numPort:=3003;
    VAR string strIPAddressClient:="";
    VAR rawbytes raw_data_out;
    VAR rawbytes raw_data_in;
    !stringHeader: Only indexs from 1 to 128 can be set, the indexs from 129 to 256 are for response command
    CONST string stringHeader{256}:=["CloseConnection","GetOperatingMode","GetRunMode","","","","","GetRobotStatus",
        "GetSignalDo","GetSignalGo","GetSignalAo","GetSignalDi","GetSignalGi","GetSignalAi","","",
        "SetSignalDo","SetSignalGo","SetSignalAo","","","","","",
        "GetNumData","SetNumData","","","","","GetDataTaskName","SetDataTaskName",
        "GetWeldData","SetWeldData","GetSeamData","SetSeamData","GetWeaveData","SetWeaveData","","",
        "","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""];

    PERS string strDataTaskName:="T_ROB1";
    PERS num numRobotStatus:=43;
    VAR num numDataInLength:=0;
    VAR num numMessageFormatError:=0;

    PROC main()
        SetTPHandlerLogLevel\WARNING;
        SetFileHandlerLogLevel\DEBUG;
        IF RobOS()=FALSE THEN
            strIPAddressServer:="127.0.0.1";
        ENDIF
        WHILE TRUE DO
            SocketCreate socketServer;
            SocketBind socketServer,strIPAddressServer,numPort;
            SocketListen socketServer;
            commandReceive;
            SocketClose socketServer;
            WaitTime 2;
        ENDWHILE
    ENDPROC

    PROC CommandReceive()
        VAR byte commandIn;
        SocketAccept socketServer,socketClient\ClientAddress:=strIPAddressClient\Time:=WAIT_MAX;
        Logging\INFO,\LoggerName:="SocketModule","Client "+strIPAddressClient+" is connected.";
        WHILE true DO
            numMessageFormatError:=0;
            ClearRawBytes raw_data_in;
            SocketReceive socketClient\RawData:=raw_data_in\Time:=WAIT_MAX;
            IF RawBytesLen(raw_data_in)>=3 THEN
                UnpackRawBytes raw_data_in\Network,1,commandIn\Hex1;
                UnpackRawBytes raw_data_in\Network,2,numDataInLength\IntX:=UINT;
                IF RawBytesLen(raw_data_in)=numDataInLength+3 THEN
                    IF commandIn=0 THEN
                        ResponseSocketCommand commandIn+128;
                        SocketClose socketClient;
                        Logging\INFO,\LoggerName:="SocketModule","The connection "+strIPAddressClient+" is closed!";
                        RETURN ;
                    ELSE
                        IF StrLen(stringHeader{commandIn+1})>0 THEN
                            Logging\DEBUG,\LoggerName:="SocketModule","Execute command "+ValToStr(commandIn+1)+" of "+stringHeader{commandIn+1};
                            %stringHeader{commandIn+1}%commandIn;
                        ELSE
                            numMessageFormatError:=3;
                        ENDIF
                    ENDIF
                ELSE
                    numMessageFormatError:=2;
                ENDIF
            ELSE
                numMessageFormatError:=1;
            ENDIF

            TEST numMessageFormatError
            CASE 1:
                Logging\ERRORING,\LoggerName:="SocketModule","The command ("+ValToStr(commandIn)+") message should be sent from client as a whole!";
                ResponseError commandIn;
            CASE 2:
                Logging\ERRORING,\LoggerName:="SocketModule","The command's ("+ValToStr(commandIn)+") data length ("+ValToStr(numDataInLength)+") is not right!";
                ResponseError commandIn;
            CASE 3:
                Logging\ERRORING,\LoggerName:="SocketModule","Command ("+ValToStr(commandIn)+") is not set in stringHeader!";
                ResponseError commandIn;
            DEFAULT:
            ENDTEST

        ENDWHILE

    ERROR
        IF ERRNO=ERR_SOCK_TIMEOUT OR ERRNO=ERR_SOCK_CLOSED THEN
            SocketClose socketClient;
            Logging\ERRORING,\LoggerName:="SocketModule","The connection "+strIPAddressClient+" is closed abnormally!";
            RETURN ;
        ELSEIF ERRNO=ERR_REFUNKPRC THEN
            Logging\ERRORING,\LoggerName:="SocketModule","ERR_REFUNKPRC!";
            TRYNEXT;
        ELSEIF ERRNO=ERR_CALLPROC THEN
            Logging\ERRORING,\LoggerName:="SocketModule","ERR_CALLPROC!";
            TRYNEXT;
        ENDIF
    ENDPROC

    PROC PackSocketHeader(byte commandOut)
        VAR rawbytes raw_data_temp;
        CopyRawBytes raw_data_out,1,raw_data_temp,1;
        ClearRawBytes raw_data_out;
        PackRawBytes commandOut,raw_data_out\Network,1\Hex1;
        PackRawBytes RawBytesLen(raw_data_temp),raw_data_out\Network,2\IntX:=UINT;
        CopyRawBytes raw_data_temp,1,raw_data_out,4;
    ENDPROC

    PROC ResponseSocketCommand(byte commandOut)
        ClearRawBytes raw_data_out;
        PackSocketHeader commandOut;
        SocketSend socketClient\RawData:=raw_data_out;
    ENDPROC

    PROC ResponseError(byte commandIn)
        VAR byte commandOut;
        commandOut:=255;
        ClearRawBytes raw_data_out;
        PackRawBytes commandIn,raw_data_out\Network,1\Hex1;
        PackRawBytes numRobotStatus,raw_data_out\Network,2\IntX:=DINT;
        PackSocketHeader commandOut;
        SocketSend socketClient\RawData:=raw_data_out;
    ENDPROC

    PROC GetOperatingMode(byte commandIn)
        VAR byte commandOut;
        VAR num numTemp;
        TEST OpMode()
        CASE OP_AUTO:
            numTemp:=1;
        CASE OP_MAN_PROG:
            numTemp:=2;
        CASE OP_MAN_TEST:
            numTemp:=3;
        DEFAULT:
            numTemp:=0;
        ENDTEST
        commandOut:=commandIn+128;
        ClearRawBytes raw_data_out;
        PackRawBytes numTemp,raw_data_out\Network,1\IntX:=DINT;
        PackSocketHeader commandOut;
        SocketSend socketClient\RawData:=raw_data_out;
    ENDPROC

    PROC GetRunMode(byte commandIn)
        VAR byte commandOut;
        VAR num numTemp;
        TEST RunMode(\Main)
        CASE RUN_CONT_CYCLE:
            numTemp:=1;
        CASE RUN_INSTR_FWD:
            numTemp:=2;
        CASE RUN_INSTR_BWD:
            numTemp:=3;
        CASE RUN_SIM:
            numTemp:=4;
        CASE RUN_STEP_MOVE:
            numTemp:=5;
        DEFAULT:
            numTemp:=0;
        ENDTEST
        commandOut:=commandIn+128;
        ClearRawBytes raw_data_out;
        PackRawBytes numTemp,raw_data_out\Network,1\IntX:=DINT;
        PackSocketHeader commandOut;
        SocketSend socketClient\RawData:=raw_data_out;
    ENDPROC

    PROC GetRobotStatus(byte commandIn)
        VAR byte commandOut;
        ClearRawBytes raw_data_out;
        commandOut:=commandIn+128;
        PackRawBytes numRobotStatus,raw_data_out\Network,1\IntX:=DINT;
        PackSocketHeader commandOut;
        SocketSend socketClient\RawData:=raw_data_out;
        Logging\DEBUG,\LoggerName:="SocketModule",stringHeader{commandIn+1}+" : "+ValToStr(numRobotStatus);
    ENDPROC

    PROC SetSignalDo(byte commandIn)
        VAR byte commandOut;
        VAR byte byteDoValue;
        VAR string strSignalName;
        VAR signaldo signaldoTemp;
        UnpackRawBytes raw_data_in\Network,4,byteDoValue\Hex1;
        UnpackRawBytes raw_data_in\Network,5,strSignalName\ASCII:=numDataInLength-1;
        AliasIO strSignalName,signaldoTemp;
        IF byteDoValue<>0 THEN
            byteDoValue:=1;
        ENDIF
        SetDO signaldoTemp,byteDoValue;
        commandOut:=commandIn+128;
        ResponseSocketCommand commandOut;
        Logging\DEBUG,\LoggerName:="SocketModule",stringHeader{commandIn+1}+" : "+strSignalName+" : "+ValToStr(byteDoValue);
    ERROR
        IF ERRNO=ERR_ALIASIO_DEF OR ERRNO=ERR_ALIASIO_TYPE OR ERRNO=ERR_NO_ALIASIO_DEF THEN
            Logging\ERRORING,\LoggerName:="SocketModule","Signal ("+strSignalName+") is not exist";
            ResponseError commandIn;
            RETURN ;
        ENDIF
    ENDPROC

    PROC SetSignalAo(byte commandIn)
        VAR byte commandOut;
        VAR num numTemp;
        VAR string strSignalName;
        VAR signalao signalTemp;
        UnpackRawBytes raw_data_in\Network,4,numTemp\Float4;
        UnpackRawBytes raw_data_in\Network,8,strSignalName\ASCII:=numDataInLength-4;
        AliasIO strSignalName,signalTemp;
        SetAO signalTemp,numTemp;
        commandOut:=commandIn+128;
        ResponseSocketCommand commandOut;
        Logging\DEBUG,\LoggerName:="SocketModule",stringHeader{commandIn+1}+" : "+strSignalName+" : "+ValToStr(numTemp);
    ERROR
        IF ERRNO=ERR_ALIASIO_DEF OR ERRNO=ERR_ALIASIO_TYPE OR ERRNO=ERR_NO_ALIASIO_DEF THEN
            Logging\ERRORING,\LoggerName:="SocketModule","Signal ("+strSignalName+") is not exist";
            ResponseError commandIn;
            RETURN ;
        ENDIF
    ENDPROC

    PROC SetSignalGo(byte commandIn)
        VAR byte commandOut;
        VAR num numTemp;
        VAR string strSignalName;
        VAR signalgo signalgoTemp;
        UnpackRawBytes raw_data_in\Network,2,numDataInLength\IntX:=UINT;
        UnpackRawBytes raw_data_in\Network,4,numTemp\IntX:=UDINT;
        UnpackRawBytes raw_data_in\Network,8,strSignalName\ASCII:=numDataInLength-4;
        AliasIO strSignalName,signalgoTemp;
        SetGO signalgoTemp,numTemp;
        commandOut:=commandIn+128;
        ResponseSocketCommand commandOut;
        Logging\DEBUG,\LoggerName:="SocketModule",stringHeader{commandIn+1}+" : "+strSignalName+" : "+ValToStr(numTemp);
    ERROR
        IF ERRNO=ERR_ALIASIO_DEF OR ERRNO=ERR_ALIASIO_TYPE OR ERRNO=ERR_NO_ALIASIO_DEF THEN
            Logging\ERRORING,\LoggerName:="SocketModule","Signal ("+strSignalName+") is not exist";
            ResponseError commandIn;
            RETURN ;
        ENDIF
    ENDPROC

    PROC GetSignalDo(byte commandIn)
        VAR byte commandOut;
        VAR byte byteDoValue;
        VAR string strSignalName;
        VAR signaldo signaldoTemp;
        VAR byte byteSignalValue;
        UnpackRawBytes raw_data_in\Network,2,numDataInLength\IntX:=UINT;
        UnpackRawBytes raw_data_in\Network,4,strSignalName\ASCII:=numDataInLength;
        Logging\DEBUG,\LoggerName:="SocketModule","GetSignalDo AliasIO"+" : "+strSignalName;
        AliasIO strSignalName,signaldoTemp;
        commandOut:=commandIn+128;
        ClearRawBytes raw_data_out;
        byteSignalValue:=signaldoTemp;
        PackRawBytes byteSignalValue,raw_data_out\Network,1\Hex1;
        PackSocketHeader commandOut;
        SocketSend socketClient\RawData:=raw_data_out;

    ERROR
        IF ERRNO=ERR_ALIASIO_DEF OR ERRNO=ERR_ALIASIO_TYPE OR ERRNO=ERR_NO_ALIASIO_DEF THEN
            Logging\ERRORING,\LoggerName:="SocketModule","Signal ("+strSignalName+") is not exist";
            ResponseError commandIn;
            RETURN ;
        ENDIF
    ENDPROC

    PROC GetSignalAo(byte commandIn)
        VAR byte commandOut;
        VAR byte byteAoValue;
        VAR string strSignalName;
        VAR signalao signalaoTemp;
        UnpackRawBytes raw_data_in\Network,2,numDataInLength\IntX:=UINT;
        UnpackRawBytes raw_data_in\Network,4,strSignalName\ASCII:=numDataInLength;
        Logging\DEBUG,\LoggerName:="SocketModule","GetSignalAo AliasIO"+" : "+strSignalName;
        AliasIO strSignalName,signalaoTemp;
        commandOut:=commandIn+128;
        ClearRawBytes raw_data_out;
        PackRawBytes AOutput(signalaoTemp),raw_data_out\Network,1\Float4;
        PackSocketHeader commandOut;
        SocketSend socketClient\RawData:=raw_data_out;

    ERROR
        IF ERRNO=ERR_ALIASIO_DEF OR ERRNO=ERR_ALIASIO_TYPE OR ERRNO=ERR_NO_ALIASIO_DEF THEN
            Logging\ERRORING,\LoggerName:="SocketModule","Signal ("+strSignalName+") is not exist";
            ResponseError commandIn;
            RETURN ;
        ENDIF
    ENDPROC

    PROC GetSignalGo(byte commandIn)
        VAR byte commandOut;
        VAR byte byteGoValue;
        VAR string strSignalName;
        VAR signalgo signalgoTemp;
        UnpackRawBytes raw_data_in\Network,2,numDataInLength\IntX:=UINT;
        UnpackRawBytes raw_data_in\Network,4,strSignalName\ASCII:=numDataInLength;
        Logging\DEBUG,\LoggerName:="SocketModule","GetSignalGo AliasIO"+" : "+strSignalName;
        AliasIO strSignalName,signalGoTemp;
        commandOut:=commandIn+128;
        ClearRawBytes raw_data_out;
        PackRawBytes GOutput(signalgoTemp),raw_data_out\Network,1\IntX:=UDINT;
        PackSocketHeader commandOut;
        SocketSend socketClient\RawData:=raw_data_out;

    ERROR
        IF ERRNO=ERR_ALIASIO_DEF OR ERRNO=ERR_ALIASIO_TYPE OR ERRNO=ERR_NO_ALIASIO_DEF THEN
            Logging\ERRORING,\LoggerName:="SocketModule","Signal ("+strSignalName+") is not exist";
            ResponseError commandIn;
            RETURN ;
        ENDIF
    ENDPROC

    PROC GetSignalDi(byte commandIn)
        VAR byte commandOut;
        VAR byte byteDiValue;
        VAR string strSignalName;
        VAR signaldi signaldiTemp;
        VAR byte byteSignalValue;
        UnpackRawBytes raw_data_in\Network,2,numDataInLength\IntX:=UINT;
        UnpackRawBytes raw_data_in\Network,4,strSignalName\ASCII:=numDataInLength;
        AliasIO strSignalName,signaldiTemp;
        commandOut:=commandIn+128;
        ClearRawBytes raw_data_out;
        byteSignalValue:=signaldiTemp;
        PackRawBytes byteSignalValue,raw_data_out\Network,1\Hex1;
        PackSocketHeader commandOut;
        SocketSend socketClient\RawData:=raw_data_out;

    ERROR
        IF ERRNO=ERR_ALIASIO_DEF OR ERRNO=ERR_ALIASIO_TYPE OR ERRNO=ERR_NO_ALIASIO_DEF THEN
            Logging\ERRORING,\LoggerName:="SocketModule","Signal ("+strSignalName+") is not exist";
            ResponseError commandIn;
            RETURN ;
        ENDIF
    ENDPROC

    PROC GetSignalAi(byte commandIn)
        VAR byte commandOut;
        VAR byte byteAiValue;
        VAR string strSignalName;
        VAR signalai signalaiTemp;
        UnpackRawBytes raw_data_in\Network,2,numDataInLength\IntX:=UINT;
        UnpackRawBytes raw_data_in\Network,4,strSignalName\ASCII:=numDataInLength;
        AliasIO strSignalName,signalaiTemp;
        commandOut:=commandIn+128;
        ClearRawBytes raw_data_out;
        PackRawBytes AInput(signalaiTemp),raw_data_out\Network,1\Float4;
        PackSocketHeader commandOut;
        SocketSend socketClient\RawData:=raw_data_out;

    ERROR
        IF ERRNO=ERR_ALIASIO_DEF OR ERRNO=ERR_ALIASIO_TYPE OR ERRNO=ERR_NO_ALIASIO_DEF THEN
            Logging\ERRORING,\LoggerName:="SocketModule","Signal ("+strSignalName+") is not exist";
            ResponseError commandIn;
            RETURN ;
        ENDIF
    ENDPROC

    PROC GetSignalGi(byte commandIn)
        VAR byte commandOut;
        VAR byte byteGiValue;
        VAR string strSignalName;
        VAR signalgi signalgiTemp;
        UnpackRawBytes raw_data_in\Network,2,numDataInLength\IntX:=UINT;
        UnpackRawBytes raw_data_in\Network,4,strSignalName\ASCII:=numDataInLength;
        AliasIO strSignalName,signalGiTemp;
        commandOut:=commandIn+128;
        ClearRawBytes raw_data_out;
        PackRawBytes GInput(signalgiTemp),raw_data_out\Network,1\IntX:=UDINT;
        PackSocketHeader commandOut;
        SocketSend socketClient\RawData:=raw_data_out;

    ERROR
        IF ERRNO=ERR_ALIASIO_DEF OR ERRNO=ERR_ALIASIO_TYPE OR ERRNO=ERR_NO_ALIASIO_DEF THEN
            Logging\ERRORING,\LoggerName:="SocketModule","Signal ("+strSignalName+") is not exist";
            ResponseError commandIn;
            RETURN ;
        ENDIF
    ENDPROC

    PROC GetNumData(byte commandIn)
        VAR byte commandOut;
        VAR string strSymbolName;
        VAR num symbolValue;
        UnpackRawBytes raw_data_in\Network,2,numDataInLength\IntX:=UINT;
        UnpackRawBytes raw_data_in\Network,4,strSymbolName\ASCII:=numDataInLength;
        GetDataVal strSymbolName\TaskName:=strDataTaskName,symbolValue;
        commandOut:=commandIn+128;
        ClearRawBytes raw_data_out;
        PackRawBytes symbolValue,raw_data_out\Network,1\Float4;
        PackSocketHeader commandOut;
        SocketSend socketClient\RawData:=raw_data_out;

    ERROR
        IF ERRNO=ERR_SYM_ACCESS OR ERRNO=ERR_INVDIM OR ERRNO=ERR_SYMBOL_TYPE OR ERRNO=ERR_TASKNAME THEN
            Logging\ERRORING,\LoggerName:="SocketModule","Symbol ("+strSymbolName+") is not exist";
            ResponseError commandIn;
            RETURN ;
        ENDIF
    ENDPROC

    PROC SetNumData(byte commandIn)
        VAR byte commandOut;
        VAR string strSymbolName;
        VAR num symbolValue;
        UnpackRawBytes raw_data_in\Network,4,symbolValue\Float4;
        UnpackRawBytes raw_data_in\Network,8,strSymbolName\ASCII:=numDataInLength-4;
        SetDataVal strSymbolName\TaskName:=strDataTaskName,symbolValue;
        commandOut:=commandIn+128;
        ResponseSocketCommand commandOut;
        Logging\DEBUG,\LoggerName:="SocketModule",stringHeader{commandIn+1}+" : "+strSymbolName+" : "+ValToStr(symbolValue);

    ERROR
        IF ERRNO=ERR_SYM_ACCESS OR ERRNO=ERR_INVDIM OR ERRNO=ERR_SYMBOL_TYPE OR ERRNO=ERR_TASKNAME THEN
            Logging\ERRORING,\LoggerName:="SocketModule","Symbol ("+strSymbolName+") is not exist";
            ResponseError commandIn;
            RETURN ;
        ENDIF
    ENDPROC

    LOCAL PROC GetWeldData(byte commandIn)
        VAR byte commandOut;
        VAR string strSymbolName;
        VAR welddata symbolValue;
        UnpackRawBytes raw_data_in\Network,2,numDataInLength\IntX:=UINT;
        UnpackRawBytes raw_data_in\Network,4,strSymbolName\ASCII:=numDataInLength;
        GetDataVal strSymbolName\TaskName:=strDataTaskName,symbolValue;
        commandOut:=commandIn+128;
        ClearRawBytes raw_data_out;
        PackRawBytes ValToStr(symbolValue),raw_data_out\Network,1\ASCII;
        PackSocketHeader commandOut;
        SocketSend socketClient\RawData:=raw_data_out;
        Logging\DEBUG,\LoggerName:="SocketModule",stringHeader{commandIn+1}+" : "+strSymbolName+" : "+ValToStr(symbolValue);
        
    ERROR
        IF ERRNO=ERR_SYM_ACCESS OR ERRNO=ERR_INVDIM OR ERRNO=ERR_SYMBOL_TYPE OR ERRNO=ERR_TASKNAME THEN
            Logging\ERRORING,\LoggerName:="SocketModule","Symbol ("+strSymbolName+") of WeldData is not exist";
            ResponseError commandIn;
            RETURN ;
        ENDIF
    ENDPROC

    LOCAL PROC SetWeldData(byte commandIn)
        VAR byte commandOut;
        VAR string strSymbolName;
        VAR string strSymbolValue;
        VAR welddata symbolValue;
        VAR string strNullCharacter:="\00";
        VAR string strCharacter:="\00";
        VAR num numCurIndex:=4;
        VAR bool boolConversionSucceeded;
        UnpackRawBytes raw_data_in\Network,numCurIndex,strCharacter\ASCII:=1;
        WHILE strCharacter<>strNullCharacter DO
            Incr numCurIndex;
            UnpackRawBytes raw_data_in\Network,numCurIndex,strCharacter\ASCII:=1;
        ENDWHILE
        IF numCurIndex=4 THEN
            Logging\ERRORING,\LoggerName:="SocketModule","No symbol name";
            ResponseError commandIn;
            RETURN ;
        ENDIF
        UnpackRawBytes raw_data_in\Network,4,strSymbolName\ASCII:=numCurIndex-4;
        UnpackRawBytes raw_data_in\Network,numCurIndex+1,strSymbolValue\ASCII:=numDataInLength+3-numCurIndex;
        boolConversionSucceeded:=StrToVal(strSymbolValue,symbolValue);
        IF boolConversionSucceeded=FALSE THEN
            Logging\ERRORING,\LoggerName:="SocketModule",strSymbolValue;
            Logging\ERRORING,\LoggerName:="SocketModule","WeldData Conversion Failed: "+ValToStr(numCurIndex)+", "+ValToStr(numDataInLength);
            ResponseError commandIn;
            RETURN ;
        ENDIF
        SetDataVal strSymbolName\TaskName:=strDataTaskName,symbolValue;
        commandOut:=commandIn+128;
        ResponseSocketCommand commandOut;
        Logging\DEBUG,\LoggerName:="SocketModule",stringHeader{commandIn+1}+" : "+strSymbolName+" : "+ValToStr(symbolValue);

    ERROR
        IF ERRNO=ERR_SYM_ACCESS OR ERRNO=ERR_INVDIM OR ERRNO=ERR_SYMBOL_TYPE OR ERRNO=ERR_TASKNAME THEN
            Logging\ERRORING,\LoggerName:="SocketModule","Symbol ("+strSymbolName+") of WeldData is not exist";
            ResponseError commandIn;
            RETURN ;
        ENDIF
    ENDPROC

    LOCAL PROC GetSeamData(byte commandIn)
        VAR byte commandOut;
        VAR string strSymbolName;
        VAR Seamdata symbolValue;
        UnpackRawBytes raw_data_in\Network,2,numDataInLength\IntX:=UINT;
        UnpackRawBytes raw_data_in\Network,4,strSymbolName\ASCII:=numDataInLength;
        GetDataVal strSymbolName\TaskName:=strDataTaskName,symbolValue;
        commandOut:=commandIn+128;
        ClearRawBytes raw_data_out;
        PackRawBytes "["+ValToStr(symbolValue.purge_time)+","+ValToStr(symbolValue.preflow_time)+",",raw_data_out\Network,RawBytesLen(raw_data_out)+1\ASCII;
        PackRawBytes ValToStr(symbolValue.ign_arc)+",",raw_data_out\Network,RawBytesLen(raw_data_out)+1\ASCII;
        PackRawBytes ValToStr(symbolValue.ign_move_delay)+","+ValToStr(symbolValue.scrape_start)+","+ValToStr(symbolValue.heat_speed)+",",raw_data_out\Network,RawBytesLen(raw_data_out)+1\ASCII;
        PackRawBytes ValToStr(symbolValue.heat_time)+","+ValToStr(symbolValue.heat_distance)+",",raw_data_out\Network,RawBytesLen(raw_data_out)+1\ASCII;
        PackRawBytes ValToStr(symbolValue.heat_arc)+",",raw_data_out\Network,RawBytesLen(raw_data_out)+1\ASCII;
        PackRawBytes ValToStr(symbolValue.cool_time)+","+ValToStr(symbolValue.fill_time)+",",raw_data_out\Network,RawBytesLen(raw_data_out)+1\ASCII;
        PackRawBytes ValToStr(symbolValue.fill_arc)+",",raw_data_out\Network,RawBytesLen(raw_data_out)+1\ASCII;
        PackRawBytes ValToStr(symbolValue.bback_time)+","+ValToStr(symbolValue.rback_time)+",",raw_data_out\Network,RawBytesLen(raw_data_out)+1\ASCII;
        PackRawBytes ValToStr(symbolValue.bback_arc)+",",raw_data_out\Network,RawBytesLen(raw_data_out)+1\ASCII;
        PackRawBytes ValToStr(symbolValue.postflow_time)+"]",raw_data_out\Network,RawBytesLen(raw_data_out)+1\ASCII;
        PackSocketHeader commandOut;
        SocketSend socketClient\RawData:=raw_data_out;

    ERROR
        IF ERRNO=ERR_SYM_ACCESS OR ERRNO=ERR_INVDIM OR ERRNO=ERR_SYMBOL_TYPE OR ERRNO=ERR_TASKNAME THEN
            Logging\ERRORING,\LoggerName:="SocketModule","Symbol ("+strSymbolName+") of SeamData is not exist";
            ResponseError commandIn;
            RETURN ;
        ENDIF
    ENDPROC

    LOCAL PROC SetSeamData(byte commandIn)
        VAR byte commandOut;
        VAR string strSymbolName;
        VAR string strSymbolValue;
        VAR Seamdata symbolValue;
        VAR string strNullCharacter:="\00";
        VAR string strCommaCharacter:=",";
        VAR string strRightBracketCharacter:="]";
        VAR string strCharacter:="\00";
        VAR num numStartIndex:=0;
        VAR num numCurIndex:=4;
        VAR bool boolConversionSucceeded;
        UnpackRawBytes raw_data_in\Network,numCurIndex,strCharacter\ASCII:=1;
        WHILE strCharacter<>strNullCharacter DO
            Incr numCurIndex;
            UnpackRawBytes raw_data_in\Network,numCurIndex,strCharacter\ASCII:=1;
        ENDWHILE
        IF numCurIndex=4 THEN
            Logging\ERRORING,\LoggerName:="SocketModule","No symbol name";
            ResponseError commandIn;
            RETURN ;
        ENDIF
        UnpackRawBytes raw_data_in\Network,4,strSymbolName\ASCII:=numCurIndex-4;

        ![0,1.5,[0,0,0,0,0,0,0,0,0],0,0,0,0,0,[0,0,0,0,0,0,0,0,0],0,0,[0,0,0,0,0,0,0,0,0],0,0,[0,0,0,0,0,0,0,0,0],0]
        numCurIndex:=numCurIndex+2;
        Logging\DEBUG,\LoggerName:="SocketModule","numCurIndex="+ValToStr(numCurIndex);
        strSymbolValue:=GetStringofSymbolValue(numCurIndex,strCommaCharacter);
        boolConversionSucceeded:=StrToVal(strSymbolValue,symbolValue.purge_time);

        strSymbolValue:=GetStringofSymbolValue(numCurIndex,strCommaCharacter);
        boolConversionSucceeded:=StrToVal(strSymbolValue,symbolValue.preflow_time);

        strSymbolValue:=GetStringofSymbolValue(numCurIndex,strRightBracketCharacter\includeDelimiter);
        boolConversionSucceeded:=StrToVal(strSymbolValue,symbolValue.ign_arc);

        Incr numCurIndex;
        strSymbolValue:=GetStringofSymbolValue(numCurIndex,strCommaCharacter);
        boolConversionSucceeded:=StrToVal(strSymbolValue,symbolValue.ign_move_delay);

        strSymbolValue:=GetStringofSymbolValue(numCurIndex,strCommaCharacter);
        boolConversionSucceeded:=StrToVal(strSymbolValue,symbolValue.scrape_start);

        strSymbolValue:=GetStringofSymbolValue(numCurIndex,strCommaCharacter);
        boolConversionSucceeded:=StrToVal(strSymbolValue,symbolValue.heat_speed);

        strSymbolValue:=GetStringofSymbolValue(numCurIndex,strCommaCharacter);
        boolConversionSucceeded:=StrToVal(strSymbolValue,symbolValue.heat_time);

        strSymbolValue:=GetStringofSymbolValue(numCurIndex,strCommaCharacter);
        boolConversionSucceeded:=StrToVal(strSymbolValue,symbolValue.heat_distance);

        strSymbolValue:=GetStringofSymbolValue(numCurIndex,strRightBracketCharacter\includeDelimiter);
        boolConversionSucceeded:=StrToVal(strSymbolValue,symbolValue.heat_arc);

        Incr numCurIndex;
        strSymbolValue:=GetStringofSymbolValue(numCurIndex,strCommaCharacter);
        boolConversionSucceeded:=StrToVal(strSymbolValue,symbolValue.cool_time);

        strSymbolValue:=GetStringofSymbolValue(numCurIndex,strCommaCharacter);
        boolConversionSucceeded:=StrToVal(strSymbolValue,symbolValue.fill_time);

        strSymbolValue:=GetStringofSymbolValue(numCurIndex,strRightBracketCharacter\includeDelimiter);
        boolConversionSucceeded:=StrToVal(strSymbolValue,symbolValue.fill_arc);

        Incr numCurIndex;
        strSymbolValue:=GetStringofSymbolValue(numCurIndex,strCommaCharacter);
        boolConversionSucceeded:=StrToVal(strSymbolValue,symbolValue.bback_time);

        strSymbolValue:=GetStringofSymbolValue(numCurIndex,strCommaCharacter);
        boolConversionSucceeded:=StrToVal(strSymbolValue,symbolValue.rback_time);

        strSymbolValue:=GetStringofSymbolValue(numCurIndex,strRightBracketCharacter\includeDelimiter);
        boolConversionSucceeded:=StrToVal(strSymbolValue,symbolValue.bback_arc);

        Incr numCurIndex;
        strSymbolValue:=GetStringofSymbolValue(numCurIndex,strRightBracketCharacter);
        boolConversionSucceeded:=StrToVal(strSymbolValue,symbolValue.postflow_time);

        SetDataVal strSymbolName\TaskName:=strDataTaskName,symbolValue;
        commandOut:=commandIn+128;
        ResponseSocketCommand commandOut;
        Logging\DEBUG,\LoggerName:="SocketModule",stringHeader{commandIn+1}+" : "+strSymbolName;

    ERROR
        IF ERRNO=ERR_SYM_ACCESS OR ERRNO=ERR_INVDIM OR ERRNO=ERR_SYMBOL_TYPE OR ERRNO=ERR_TASKNAME THEN
            Logging\ERRORING,\LoggerName:="SocketModule","Symbol ("+strSymbolName+") of SeamData is not exist";
            ResponseError commandIn;
            RETURN ;
        ENDIF
    ENDPROC

    FUNC string GetStringofSymbolValue(inout num numStartIndex,string strDelimiterCharacter\switch includeDelimiter)
        VAR num numCurIndex;
        VAR string strCharacter;
        VAR string strSymbolValue;
        numCurIndex:=numStartIndex;
        UnpackRawBytes raw_data_in\Network,numCurIndex,strCharacter\ASCII:=1;
        WHILE strCharacter<>strDelimiterCharacter DO
            Incr numCurIndex;
            UnpackRawBytes raw_data_in\Network,numCurIndex,strCharacter\ASCII:=1;
        ENDWHILE
        IF Present(includeDelimiter) THEN
            UnpackRawBytes raw_data_in\Network,numStartIndex,strSymbolValue\ASCII:=numCurIndex+1-numStartIndex;
        ELSE
            UnpackRawBytes raw_data_in\Network,numStartIndex,strSymbolValue\ASCII:=numCurIndex-numStartIndex;
        ENDIF
        numStartIndex:=numCurIndex+1;
        !        Logging\DEBUG,\LoggerName:="SocketModule","numCurIndex="+ValToStr(numCurIndex);
        !        Logging\DEBUG,\LoggerName:="SocketModule",strSymbolValue;
        RETURN strSymbolValue;
    ENDFUNC

    LOCAL PROC GetWeaveData(byte commandIn)
        VAR byte commandOut;
        VAR string strSymbolName;
        VAR Weavedata symbolValue;
        UnpackRawBytes raw_data_in\Network,2,numDataInLength\IntX:=UINT;
        UnpackRawBytes raw_data_in\Network,4,strSymbolName\ASCII:=numDataInLength;
        GetDataVal strSymbolName\TaskName:=strDataTaskName,symbolValue;
        commandOut:=commandIn+128;
        ClearRawBytes raw_data_out;
        PackRawBytes ValToStr(symbolValue),raw_data_out\Network,1\ASCII;
        PackSocketHeader commandOut;
        SocketSend socketClient\RawData:=raw_data_out;

    ERROR
        IF ERRNO=ERR_SYM_ACCESS OR ERRNO=ERR_INVDIM OR ERRNO=ERR_SYMBOL_TYPE OR ERRNO=ERR_TASKNAME THEN
            Logging\ERRORING,\LoggerName:="SocketModule","Symbol ("+strSymbolName+") of WeaveData is not exist";
            ResponseError commandIn;
            RETURN ;
        ENDIF
    ENDPROC

    LOCAL PROC SetWeaveData(byte commandIn)
        VAR byte commandOut;
        VAR string strSymbolName;
        VAR string strSymbolValue;
        VAR Weavedata symbolValue;
        VAR string strNullCharacter:="\00";
        VAR string strCharacter:="\00";
        VAR num numCurIndex:=4;
        VAR bool boolConversionSucceeded;
        UnpackRawBytes raw_data_in\Network,numCurIndex,strCharacter\ASCII:=1;
        WHILE strCharacter<>strNullCharacter DO
            Incr numCurIndex;
            UnpackRawBytes raw_data_in\Network,numCurIndex,strCharacter\ASCII:=1;
        ENDWHILE
        IF numCurIndex=4 THEN
            Logging\ERRORING,\LoggerName:="SocketModule","No symbol name";
            ResponseError commandIn;
            RETURN ;
        ENDIF
        UnpackRawBytes raw_data_in\Network,4,strSymbolName\ASCII:=numCurIndex-4;
        UnpackRawBytes raw_data_in\Network,numCurIndex+1,strSymbolValue\ASCII:=numDataInLength+3-numCurIndex;
        boolConversionSucceeded:=StrToVal(strSymbolValue,symbolValue);
        IF boolConversionSucceeded=FALSE THEN
            Logging\ERRORING,\LoggerName:="SocketModule",strSymbolValue;
            Logging\ERRORING,\LoggerName:="SocketModule","WeaveData Conversion Failed: "+ValToStr(numCurIndex)+", "+ValToStr(numDataInLength);
            ResponseError commandIn;
            RETURN ;
        ENDIF
        SetDataVal strSymbolName\TaskName:=strDataTaskName,symbolValue;
        commandOut:=commandIn+128;
        ResponseSocketCommand commandOut;
        Logging\DEBUG,\LoggerName:="SocketModule",stringHeader{commandIn+1}+" : "+strSymbolName+" : "+ValToStr(symbolValue);

    ERROR
        IF ERRNO=ERR_SYM_ACCESS OR ERRNO=ERR_INVDIM OR ERRNO=ERR_SYMBOL_TYPE OR ERRNO=ERR_TASKNAME THEN
            Logging\ERRORING,\LoggerName:="SocketModule","Symbol ("+strSymbolName+") of WeaveData is not exist";
            ResponseError commandIn;
            RETURN ;
        ENDIF
    ENDPROC

ENDMODULE