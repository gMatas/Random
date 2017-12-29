#!/bin/bash

## File overlord.conf is used to store separate values
# for different needs within these template:
# Section "SECTION_NAME"
#     "OPTION_NAME" "OPTION_VALUE"
# EndSection

# Section "Touchpad1710"
#     "TouchpadOff" "0"
#     "TapButton1" "1"
#     "TapButton2" "2"
#     "TapButton3" "3"
# EndSection


confFile="`dirname ${BASH_SOURCE}`/overlord.conf"
sectionName="Touchpad1710"

## Runs on user login and restores previous session synclient configurations
function onLoginSequence {
    # make the scroll delta values negative
    scrollDelta=`synclient | grep "VertScrollDelta" | cut -d'=' -f2`
    if [ $scrollDelta -gt 0 ]
    then
        synclient VertScrollDelta=$((scrollDelta * -1))
    fi
    scrollDelta=`synclient | grep "HorizScrollDelta" | cut -d'=' -f2`
    if [ $scrollDelta -gt 0 ]
    then
        synclient HorizScrollDelta=$((scrollDelta * -1))
    fi
    # get and set synclient configuration settings
    confList=($(sed -n "/^\s*$/d; /Section *\"${sectionName}\"/, /EndSection/p" < $confFile |
        sed '1d;$d' | cut -s --output-delimiter='=' -d'"' -f2,4))
    for conf in ${confList[@]}
    do
        synclient $conf
    done
    notify-send "OVERLORD:" "overlord.conf file has been loaded successfully!"
}

## Touchpad toggle
function touchpadTurnOnOffToggle {
    isTouchpadOff=`synclient | grep "TouchpadOff" | cut -d'=' -f2`
    if [ $isTouchpadOff -eq 0 ] || [ $isTouchpadOff -eq 1 ]
    then
        let isTouchpadOff^=1
        synclient TouchpadOff=$isTouchpadOff
        setOptions
        if [ $isTouchpadOff -eq 0 ]
        then
            notify-send "OVERLORD:" "Touchpad On"
        else
            notify-send "OVERLORD:" "Touchpad Off"
        fi
    else
        synclient TouchpadOff=0
    fi
}

function setOptions {
    awk -F ' ' -v sn=$sectionName -v opt_names="TouchpadOff" -v opt_values=$isTouchpadOff \
    'BEGIN {
        split(opt_names, _opt_names, ";")
        split(opt_values, _opt_values, ";")
    }
    {
        if ($0 ~ "Section *\"" sn "\" *") {
            _isSectionFound = 1
        }
        if (_isSectionFound && $0 ~ "EndSection") {
            _isSectionFound = 0
        }
        if (_isSectionFound) {
            _isChanged = 0
            for (i in _opt_names) {
                if ($1 ~ "\""_opt_names[i]"\"") {
                    print("    \""_opt_names[i]"\" \""_opt_values[i]"\"")
                    _isChanged = 1
                    break
                }
            }
            if (_isChanged == 0) {
                print $0
            }
        }
        else {
            print $0
        }
    }' $confFile > $confFile.temp
    cat $confFile.temp > $confFile
    rm $confFile.temp
}

## Main script logic controller
if [ $# == 1 ]
then
    case $1 in
    "--load-conf")
        onLoginSequence
    ;;
    "--toggle")
        touchpadTurnOnOffToggle
    ;;
    *)
        echo "ERR: Unrecognised argument ${1}."
        exit 1
    esac
else
    echo "ERR: Enter correct arguments."
    exit 1
fi
